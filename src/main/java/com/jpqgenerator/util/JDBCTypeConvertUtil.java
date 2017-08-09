package com.jpqgenerator.util;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC类型转换工具
 */
public class JDBCTypeConvertUtil {
    private static Map<Integer,Class> jdbcTypeWrapperClassMap;
    private static Map<Integer,Class> jdbcTypeMap;
    private static Map<Integer,String> jdbcTypeNameMap;

    static {
        jdbcTypeWrapperClassMap=new HashMap<Integer,Class>();
        jdbcTypeWrapperClassMap.put(Types.ARRAY, Object.class);
        jdbcTypeWrapperClassMap.put(Types.BIGINT, Long.class);
        jdbcTypeWrapperClassMap.put(Types.BINARY, Byte[].class);
        jdbcTypeWrapperClassMap.put(Types.BIT, Boolean.class);
        jdbcTypeWrapperClassMap.put(Types.BLOB, Byte[].class);
        jdbcTypeWrapperClassMap.put(Types.BOOLEAN,Boolean.class);
        jdbcTypeWrapperClassMap.put(Types.CHAR, String.class);
        jdbcTypeWrapperClassMap.put(Types.CLOB, String.class);
        jdbcTypeWrapperClassMap.put(Types.DATALINK, Object.class);
        jdbcTypeWrapperClassMap.put(Types.DATE, Date.class);
        jdbcTypeWrapperClassMap.put(Types.DECIMAL, BigDecimal.class);
        jdbcTypeWrapperClassMap.put(Types.DISTINCT, Object.class);
        jdbcTypeWrapperClassMap.put(Types.DOUBLE, Double.class);
        jdbcTypeWrapperClassMap.put(Types.FLOAT, Double.class);
        jdbcTypeWrapperClassMap.put(Types.INTEGER, Integer.class);
        jdbcTypeWrapperClassMap.put(Types.JAVA_OBJECT, Object.class);
        jdbcTypeWrapperClassMap.put(Types.LONGNVARCHAR, String.class);
        jdbcTypeWrapperClassMap.put(Types.LONGVARBINARY, Byte[].class);
        jdbcTypeWrapperClassMap.put(Types.LONGVARCHAR, String.class);
        jdbcTypeWrapperClassMap.put(Types.NCHAR, String.class);
        jdbcTypeWrapperClassMap.put(Types.NCLOB, String.class);
        jdbcTypeWrapperClassMap.put(Types.NVARCHAR,String.class);
        jdbcTypeWrapperClassMap.put(Types.NULL, Object.class);
        jdbcTypeWrapperClassMap.put(Types.NUMERIC, BigDecimal.class);
        jdbcTypeWrapperClassMap.put(Types.OTHER, Object.class);
        jdbcTypeWrapperClassMap.put(Types.REAL, Float.class);
        jdbcTypeWrapperClassMap.put(Types.REF, Object.class);
        jdbcTypeWrapperClassMap.put(Types.SMALLINT, Short.class);
        jdbcTypeWrapperClassMap.put(Types.STRUCT, Object.class);
        jdbcTypeWrapperClassMap.put(Types.TIME, Date.class);
        jdbcTypeWrapperClassMap.put(Types.TIMESTAMP, Date.class);
        jdbcTypeWrapperClassMap.put(Types.TINYINT, Byte.class);
        jdbcTypeWrapperClassMap.put(Types.VARBINARY, Byte[].class);
        jdbcTypeWrapperClassMap.put(Types.VARCHAR, String.class);

        jdbcTypeMap=new HashMap<Integer,Class>();
        jdbcTypeMap.put(Types.ARRAY, Object.class);
        jdbcTypeMap.put(Types.BIGINT, long.class);
        jdbcTypeMap.put(Types.BINARY, byte[].class);
        jdbcTypeMap.put(Types.BIT, boolean.class);
        jdbcTypeMap.put(Types.BLOB, byte[].class);
        jdbcTypeMap.put(Types.BOOLEAN,boolean.class);
        jdbcTypeMap.put(Types.CHAR, String.class);
        jdbcTypeMap.put(Types.CLOB, String.class);
        jdbcTypeMap.put(Types.DATALINK, Object.class);
        jdbcTypeMap.put(Types.DATE, Date.class);
        jdbcTypeMap.put(Types.DECIMAL, BigDecimal.class);
        jdbcTypeMap.put(Types.DISTINCT, Object.class);
        jdbcTypeMap.put(Types.DOUBLE, double.class);
        jdbcTypeMap.put(Types.FLOAT, double.class);
        jdbcTypeMap.put(Types.INTEGER, int.class);
        jdbcTypeMap.put(Types.JAVA_OBJECT, Object.class);
        jdbcTypeMap.put(Types.LONGNVARCHAR, String.class);
        jdbcTypeMap.put(Types.LONGVARBINARY, byte[].class);
        jdbcTypeMap.put(Types.LONGVARCHAR, String.class);
        jdbcTypeMap.put(Types.NCHAR, String.class);
        jdbcTypeMap.put(Types.NCLOB, String.class);
        jdbcTypeMap.put(Types.NVARCHAR,String.class);
        jdbcTypeMap.put(Types.NULL, Object.class);
        jdbcTypeMap.put(Types.NUMERIC, BigDecimal.class);
        jdbcTypeMap.put(Types.OTHER, Object.class);
        jdbcTypeMap.put(Types.REAL, float.class);
        jdbcTypeMap.put(Types.REF, Object.class);
        jdbcTypeMap.put(Types.SMALLINT, short.class);
        jdbcTypeMap.put(Types.STRUCT, Object.class);
        jdbcTypeMap.put(Types.TIME, Date.class);
        jdbcTypeMap.put(Types.TIMESTAMP, Date.class);
        jdbcTypeMap.put(Types.TINYINT, byte.class);
        jdbcTypeMap.put(Types.VARBINARY, byte[].class);
        jdbcTypeMap.put(Types.VARCHAR, String.class);

        jdbcTypeNameMap=new HashMap<Integer,String>();
        jdbcTypeNameMap.put(Types.ARRAY, "ARRAY");
        jdbcTypeNameMap.put(Types.DOUBLE, "DOUBLE");
        jdbcTypeNameMap.put(Types.FLOAT, "FLOAT");
        jdbcTypeNameMap.put(Types.INTEGER, "INTEGER");
        jdbcTypeNameMap.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
        jdbcTypeNameMap.put(Types.LONGVARBINARY, "LONGVARBINARY");
        jdbcTypeNameMap.put(Types.LONGVARCHAR, "LONGVARCHAR");
        jdbcTypeNameMap.put(Types.NCHAR, "NCHAR");
        jdbcTypeNameMap.put(Types.NCLOB, "NCLOB");
        jdbcTypeNameMap.put(Types.NVARCHAR, "NVARCHAR");
        jdbcTypeNameMap.put(Types.LONGNVARCHAR, "LONGNVARCHAR");
        jdbcTypeNameMap.put(Types.NULL, "NULL");
        jdbcTypeNameMap.put(Types.NUMERIC, "NUMERIC");
        jdbcTypeNameMap.put(Types.OTHER, "OTHER");
        jdbcTypeNameMap.put(Types.REAL, "REAL");
        jdbcTypeNameMap.put(Types.REF, "REF");
        jdbcTypeNameMap.put(Types.SMALLINT, "SMALLINT");
        jdbcTypeNameMap.put(Types.STRUCT, "STRUCT");
        jdbcTypeNameMap.put(Types.TIME, "TIME");
        jdbcTypeNameMap.put(Types.TIMESTAMP, "TIMESTAMP");
        jdbcTypeNameMap.put(Types.TINYINT, "TINYINT");
        jdbcTypeNameMap.put(Types.VARBINARY, "VARBINARY");
        jdbcTypeNameMap.put(Types.VARCHAR, "VARCHAR");
    }

    /**
     * 获取jdbc类型名称
     * @param jdbcTypeId
     * @return
     */
    public static String getJDBCTypeName(int jdbcTypeId){
        return jdbcTypeNameMap.get(jdbcTypeId);
    }

    /**
     * 获取对应java类型
     * @param jdbcTypeId
     * @param isWrapperClass
     * @return
     */
    public static Class toJavaType(int jdbcTypeId,boolean isWrapperClass){
        if(isWrapperClass){
            return jdbcTypeWrapperClassMap.get(jdbcTypeId);
        }else {
            return jdbcTypeMap.get(jdbcTypeId);
        }
    }

}