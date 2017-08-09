package com.jpqgenerator.db;

import com.jpqgenerator.util.JDBCTypeConvertUtil;

/**
 * 数据库类型对象
 */
public class DBType {
    //原始数据库类型
    private String sqlType;
    //JDBC类型
    private String jdbcType;
    //对应Java类型
    private Class javaTypeClass;

    /**
     * 构造方法
     * @param sqlType
     * @param jdbcTypeId
     * @param isWrapperClass 是否使用包装类型
     */
    public DBType(String sqlType, int jdbcTypeId, boolean isWrapperClass){
        this.sqlType = sqlType;
        this.jdbcType = JDBCTypeConvertUtil.getJDBCTypeName(jdbcTypeId);
        this.javaTypeClass= JDBCTypeConvertUtil.toJavaType(jdbcTypeId,isWrapperClass);
    }

    /**
     * 获取sql类型
     * @return
     */
    public String getSqlType(){
        return sqlType;
    }

    /**
     * 获取jdbc类型
     * @return
     */
    public String getJdbcType(){
        return jdbcType;
    }


    /**
     * 获取java类型
     * @return
     */
    public String getJavaType(){
        return javaTypeClass!=null?javaTypeClass.getSimpleName():"";
    }

    /**
     * 获取java类型依赖的包
     * @return
     */
    public String getJavaTypePackage(){
        if(javaTypeClass.getPackage()!=null){
            return javaTypeClass!=null?javaTypeClass.getPackage().getName():"";
        }else{
            return "";
        }
    }
}
