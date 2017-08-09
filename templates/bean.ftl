<#-- 包设置 -->
package ${package}
<#-- 依赖包导入 -->
<#list table.dependentPackages as dependentPackage>
<#if dependentPackage_index == 0 >

</#if>
import ${dependentPackage};
</#list>

<#-- 注释设置 -->
<#if table.remarks!="" >
//${table.remarks?replace("\r\n|\r|\n| +", " ", "r")}
</#if>
public class ${attributes.className}{
    <#-- 主键字段 -->
    private ${table.pkColumn.type.javaType} ${table.pkColumn.name.fieldType};<#if table.pkColumn.remarks!="" >//${table.pkColumn.remarks?replace("\r\n|\r|\n| +", " ", "r")}</#if>
    <#-- 主/外键以外其他键的字段 -->
    <#list table.columns as column>
    private ${column.type.javaType} ${column.name.fieldType};<#if column.remarks!="" >//${column.remarks?replace("\r\n|\r|\n| +", " ", "r")}</#if>
    </#list>
    <#-- 外键的字段 -->
    <#list table.fkColumns as column>
    private ${column.type.javaType} ${column.name.fieldType};<#if column.remarks!="" >//${column.remarks?replace("\r\n|\r|\n| +", " ", "r")}</#if>
    </#list>

    <#-- 主键的get方法 -->
    public ${table.pkColumn.type.javaType} get${table.pkColumn.name.classType}(){
        return ${table.pkColumn.name.fieldType};
    };

    <#-- 主键的set方法 -->
    public void set${table.pkColumn.name.classType}(${table.pkColumn.type.javaType} ${table.pkColumn.name.fieldType}){
        this.${table.pkColumn.name.fieldType} = ${table.pkColumn.name.fieldType};
    };
    <#-- 主/外键以外其他键的get/set方法 -->
    <#list table.columns as column>

    public ${column.type.javaType} get${column.name.classType}(){
        return ${column.name.fieldType};
    };

    public void set${column.name.classType}(${column.type.javaType} ${column.name.fieldType}){
        this.${column.name.fieldType} = ${column.name.fieldType};
    };
    </#list>
    <#-- 外键的get/set方法 -->
    <#list table.fkColumns as column>

    public ${column.type.javaType} get${column.name.classType}(){
        return ${column.name.fieldType};
    };

    public void set${column.name.classType}(${column.type.javaType} ${column.name.fieldType}){
        this.${column.name.fieldType} = ${column.name.fieldType};
    };
    </#list>
    
}