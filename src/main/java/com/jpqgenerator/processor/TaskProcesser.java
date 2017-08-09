package com.jpqgenerator.processor;

import com.jpqgenerator.config.Config;
import com.jpqgenerator.config.ConfigException;
import com.jpqgenerator.db.DBTable;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 任务文件处理
 */
public class TaskProcesser extends Processor {
    private Connection connection;
    private Config scanTableConfig;
    private List<Config> targetConfigs;
    private String filePath;
    private String globalEncoding;
    private Map<String,Object> globalAttributeMap;
    private Map<String,Object> contextAttributeMap;
    private Map<String,Object> taskAttributeMap;

    public TaskProcesser(Config taskConfig, Connection connection, Map<String, Object> globalAttributeMap, Map<String, Object> contextAttributeMap) throws ConfigException {
        this.scanTableConfig=taskConfig.getChildConfig("scan-table");
        this.targetConfigs=taskConfig.getChildConfig("target-list").getChildConfigs("target");
        this.filePath=taskConfig.getChildConfig("target-list").getAttr("file-path");
        this.globalEncoding =taskConfig.getChildConfig("target-list").getAttrOrDefaultValue("encoding","utf-8");
        this.connection=connection;
        this.globalAttributeMap=globalAttributeMap;
        this.contextAttributeMap=contextAttributeMap;
        Config taskAttributes=null;
        try {
            taskAttributes=taskConfig.getChildConfig("properties");
        }catch (ConfigException e){

        }
        if(taskAttributes!=null){
            taskAttributeMap=taskAttributes.getPropertyMap();
        }
    }

    public void process() throws InterruptedException, ExecutionException, SQLException, ClassNotFoundException, MalformedURLException, ConfigException, InstantiationException, IllegalAccessException {
        DBProcessor dbProcessor=new DBProcessor(scanTableConfig,connection);
        List<DBTable> dbTables=dbProcessor.paresDBTables();
        List<Future<Boolean>> futures=new ArrayList<Future<Boolean>>();
        for(Config tmpConfig:targetConfigs){
            futures.add(getExecutor().submit(new Task(tmpConfig,dbTables)));
        }
        for(Future<Boolean> tmpFuture:futures){
            tmpFuture.get();
        }
    }

    public class Task implements Callable<Boolean> {
        private List<DBTable> dbTables;
        private Config targetConfig;

        public Task(Config targetConfig,List<DBTable> dbTables){
            this.dbTables=dbTables;
            this.targetConfig=targetConfig;
        }

        @Override
        public Boolean call() throws ConfigException, ExecutionException, InterruptedException {
            TargetProcessor targetProcessor=new TargetProcessor(dbTables,targetConfig,filePath,globalEncoding,globalAttributeMap,contextAttributeMap,taskAttributeMap);
            targetProcessor.process();
            return true;
        }
    }

}
