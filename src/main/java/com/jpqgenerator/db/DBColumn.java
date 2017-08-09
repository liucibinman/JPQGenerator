package com.jpqgenerator.db;

/**
 * 数据库列对象
 */
public class DBColumn {
    //列名
    private DBName name;
    //列注释
    private String remarks;
    //列类型
    private DBType type;

    /**
     * 获取列名
     * @return
     */
    public DBName getName() {
        return name;
    }

    /**
     * 设置列名
     * @param name
     */
    public void setName(DBName name) {
        this.name = name;
    }

    /**
     * 获取列注释
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置列注释
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取列类型
     * @return
     */
    public DBType getType() {
        return type;
    }

    /**
     * 设置列类型
     * @param type
     */
    public void setType(DBType type) {
        this.type = type;
    }
}
