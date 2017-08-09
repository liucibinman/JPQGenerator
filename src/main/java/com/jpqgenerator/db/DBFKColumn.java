package com.jpqgenerator.db;

public class DBFKColumn extends DBColumn {
    //外键引用表名
    private DBName refTableName;
    //外键引用列名
    private DBName refColunmName;

    public DBName getRefTableName() {
        return refTableName;
    }

    public void setRefTableName(DBName refTableName) {
        this.refTableName = refTableName;
    }

    public DBName getRefColunmName() {
        return refColunmName;
    }

    public void setRefColunmName(DBName refColunmName) {
        this.refColunmName = refColunmName;
    }
}
