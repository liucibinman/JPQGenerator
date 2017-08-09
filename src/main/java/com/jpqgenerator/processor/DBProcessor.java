package com.jpqgenerator.processor;

import com.jpqgenerator.config.Config;
import com.jpqgenerator.config.ConfigException;
import com.jpqgenerator.db.*;

import java.net.MalformedURLException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * 数据库处理
 */
public class DBProcessor extends Processor{
    private DatabaseMetaData metaData;
    private boolean isScanAll;
    private boolean isWrapperClass;
    private String tableNamingStyle;
    private String columnNamingStyle;
    private Set<String> includeTable=null;
    private Set<Pattern> includeTableRex=null;
    private Set<String> excludeTable=null;
    private Set<Pattern> excludeTableRex=null;
    private Map<String,Map<String,DBFKColumn>> fkColumnMap=null;

    public DBProcessor(Config scanTableConfig, Connection connection) throws ClassNotFoundException, SQLException, MalformedURLException, IllegalAccessException, InstantiationException, ConfigException {
        isScanAll=Boolean.parseBoolean(scanTableConfig.getAttr("scan-all"));
        isWrapperClass=Boolean.parseBoolean(scanTableConfig.getAttrOrDefaultValue("wrapper-class","true"));
        tableNamingStyle=scanTableConfig.getAttr("table-naming-style");
        columnNamingStyle=scanTableConfig.getAttr("column-naming-style");
        boolean isRex;

        if(isScanAll){
            excludeTable=new HashSet<String>();
            excludeTableRex=new HashSet<Pattern>();
            List<Config> excludeConfigs=null;
            try{
                excludeConfigs=scanTableConfig.getChildConfigs("exclude");
            }catch (ConfigException e){

            }

            if(excludeConfigs!=null){
                for(Config tmpConfig:excludeConfigs){
                    if(tmpConfig.getAttr("table").equals("")){
                        continue;
                    }
                    isRex=Boolean.parseBoolean(tmpConfig.getAttrOrDefaultValue("rex","false"));
                    if(isRex){
                        excludeTableRex.add(Pattern.compile(tmpConfig.getAttr("table")));
                    }else{
                        excludeTable.add(tmpConfig.getAttr("table"));
                    }
                }
            }
        }else{
            includeTable=new HashSet<String>();
            includeTableRex=new HashSet<Pattern>();
            List<Config> includeConfigs=null;
            try{
                includeConfigs=scanTableConfig.getChildConfigs("include");
            }catch (ConfigException e){

            }
            if(includeConfigs!=null){
                for(Config tmpConfig:includeConfigs){
                    if(tmpConfig.getAttr("table").equals("")){
                        continue;
                    }
                    isRex=Boolean.parseBoolean(tmpConfig.getAttrOrDefaultValue("rex","false"));
                    if(isRex){
                        includeTableRex.add(Pattern.compile(tmpConfig.getAttr("table")));
                    }else{
                        includeTable.add(tmpConfig.getAttr("table"));
                    }
                }
            }
        }
        metaData=connection.getMetaData();
        fkColumnMap=new HashMap<String,Map<String,DBFKColumn>>();
    }

    public List<DBTable> paresDBTables() throws SQLException, ExecutionException, InterruptedException {
        List<DBTable> tables=new ArrayList<DBTable>();
        String tableName=null;
        String remarks=null;
        DBFKColumn fkColumn=null;
        ResultSet tableRS=metaData.getTables(null,null,"%",new String[]{"TABLE"});
        //获取外键信息
        while (tableRS.next()){
            tableName=tableRS.getString("TABLE_NAME");
            ResultSet foreignKeyRS = metaData.getExportedKeys(null, null, tableName);
            while (foreignKeyRS.next()) {
                String pkTableName = foreignKeyRS.getString("PKTABLE_NAME");//主键表名
                String pkColumnName = foreignKeyRS.getString("PKCOLUMN_NAME");//主键列名
                String fkTableName = foreignKeyRS.getString("FKTABLE_NAME");//外键表名
                String fkColumnName = foreignKeyRS.getString("FKCOLUMN_NAME");//外键列名
                fkColumn=new DBFKColumn();
                fkColumn.setRefTableName(new DBName(tableNamingStyle,pkTableName));
                fkColumn.setRefColunmName(new DBName(columnNamingStyle,pkColumnName));
                if(!fkColumnMap.containsKey(fkTableName)){
                    fkColumnMap.put(fkTableName,new HashMap<String,DBFKColumn>());
                }
                fkColumnMap.get(fkTableName).put(fkColumnName,fkColumn);
            }
        }
        tableRS.first();
        List<Future<DBTable>> futures=new ArrayList<Future<DBTable>>();
        if(isScanAll){
            while (tableRS.next()){
                tableName=tableRS.getString("TABLE_NAME");
                remarks=tableRS.getString("REMARKS");
                if(excludeTable.contains(tableName)){
                    for(Pattern tmpPattern:excludeTableRex){
                        if(tmpPattern.matcher(tableName).matches()){
                            continue;
                        }
                    }
                }
                futures.add(getExecutor().submit(new Task(tableName,remarks)));
            }
        }else{
            boolean flag;
            while (tableRS.next()){
                tableName=tableRS.getString("TABLE_NAME");
                remarks=tableRS.getString("REMARKS");
                if(!includeTable.contains(tableName)){
                    flag=true;
                    for(Pattern tmpPattern:includeTableRex){
                        if(tmpPattern.matcher(tableName).matches()){
                           flag=false;
                        }
                    }
                    if(flag){
                        continue;
                    }
                }
                futures.add(getExecutor().submit(new Task(tableName,remarks)));
            }
        }
        for(Future<DBTable> tmpFuture:futures){
            tables.add(tmpFuture.get());
        }
        return tables;
    }

    private class Task implements Callable<DBTable>{
        private String tableName;
        private String remarks;

        public Task(String tableName,String remarks){
            this.tableName=tableName;
            this.remarks=remarks;
        }

        @Override
        public DBTable call() throws SQLException {
            Set<String>  dependentPackages=null;
            String tmpPackage=null;

            DBTable table=new DBTable();
            table.setName(new DBName(tableNamingStyle,tableName));
            table.setRemarks(remarks);

            ResultSet primaryKeyRS = metaData.getPrimaryKeys(null,null,tableName);
            String primaryKeyName=null;
            if (primaryKeyRS.next()){
                primaryKeyName=primaryKeyRS.getString("COLUMN_NAME");
            }

            ResultSet columnRS = metaData.getColumns(null,null,tableName,null);
            List<DBColumn> columns=new ArrayList<DBColumn>();
            List<DBFKColumn> fkColumns=new ArrayList<DBFKColumn>();
            DBFKColumn fkColumn=null;
            DBColumn column=null;
            String columnName=null;
            while (columnRS.next()){
                column=new DBColumn();
                columnName=columnRS.getString("COLUMN_NAME");
                column.setName(new DBName(columnNamingStyle,columnRS.getString("COLUMN_NAME")));
                column.setRemarks(columnRS.getString("REMARKS"));
                column.setType(new DBType(columnRS.getString("TYPE_NAME"),columnRS.getInt("DATA_TYPE"),isWrapperClass));
                if(columnName.equals(primaryKeyName)){
                    table.setPkColumn(column);
                }else{
                    if(fkColumnMap.containsKey(tableName)&&fkColumnMap.get(tableName).containsKey(columnName)){
                        fkColumn=fkColumnMap.get(tableName).get(columnName);
                        fkColumn.setName(column.getName());
                        fkColumn.setRemarks(column.getRemarks());
                        fkColumn.setType(column.getType());
                        fkColumns.add(fkColumn);
                    }else{
                        columns.add(column);
                    }

                }
            }
            table.setFkColumns(fkColumns);
            table.setColumns(columns);

            List<DBColumn> allColumns=new ArrayList<DBColumn>();
            allColumns.add(table.getPkColumn());
            allColumns.addAll(columns);
            allColumns.addAll(table.getFkColumns());
            table.setAllColums(allColumns);

            dependentPackages=new HashSet<String>();
            for(DBColumn tmpColumn:allColumns){
                tmpPackage=tmpColumn.getType().getJavaTypePackage();
                if(!tmpPackage.equals("java.lang")&&!tmpPackage.equals("")){
                    dependentPackages.add(tmpPackage);
                }
            }
            table.setDependentPackages(dependentPackages);
            return table;
        }
    }
}
