package com.jpqgenerator.db;

import java.util.List;
import java.util.Set;

/**
 * 数据库表对象
 */
public class DBTable {
    //表名
    private DBName name;
    //表注释
    private String remarks;
    //主键列
    private DBColumn pkColumn;
    //外键列
    private List<DBFKColumn> fkColumns;
    //其余列
    private List<DBColumn> columns;
    //所有列
    private List<DBColumn> allColums;
    //依赖的包集合
    private Set<String> dependentPackages;

    /**
     * 获取表名
     * @return
     */
    public DBName getName() {
        return name;
    }

    /**
     * 设置表名
     * @param name
     */
    public void setName(DBName name) {
        this.name = name;
    }

    /**
     * 获取表注释
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置表注释
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     *  获取主键列
     * @return
     */
    public DBColumn getPkColumn() {
        return pkColumn;
    }

    /**
     * 设置主键列
     * @param pkColumn
     */
    public void setPkColumn(DBColumn pkColumn) {
        this.pkColumn = pkColumn;
    }

    /**
     *  设置外键列
     * @return
     */
    public List<DBFKColumn> getFkColumns() {
        return fkColumns;
    }

    /**
     *  设置外键列
     * @return
     */
    public void setFkColumns(List<DBFKColumn> fkColumns) {
        this.fkColumns = fkColumns;
    }

    /**
     * 获取其余列
     * @return
     */
    public List<DBColumn> getColumns() {
        return columns;
    }

    /**
     * 设置其余列
     * @param columns
     */
    public void setColumns(List<DBColumn> columns) {
        this.columns = columns;
    }

    /**
     * 获取所有列
     * @return
     */
    public List<DBColumn> getAllColums() {
        return allColums;
    }

    /**
     * 设置所有列
     * @param allColums
     */
    public void setAllColums(List<DBColumn> allColums) {
        this.allColums = allColums;
    }

    /**
     * 获取依赖的包集合
     * @return
     */
    public Set<String> getDependentPackages() {
        return dependentPackages;
    }

    /**
     * 设置依赖的包集合
     * @param dependentPackages
     */
    public void setDependentPackages(Set<String> dependentPackages) {
        this.dependentPackages = dependentPackages;
    }
}
