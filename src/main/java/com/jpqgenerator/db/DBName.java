package com.jpqgenerator.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库名字对象
 */
public class DBName {
    public static final String LOWER_CAMEL_CASE="lower-camel-case";
    public static final String UPPER_CAMEL_CASE="upper-camel-case";
    public static final String UNDER_SCORE_CASE="under-score-case";

    //名字数组
    private List<String> nameList;
    //命名风格
    private String namingStyle;

    /**
     * 构造方法
     * @param name 数据库原始名称
     */
    public DBName(String namingStyle,String name){
        this.namingStyle=namingStyle;
        if(namingStyle.equals(DBName.LOWER_CAMEL_CASE)){
            nameList =new ArrayList<String>();
            StringBuffer buffer=new StringBuffer();
            for(char c:name.toCharArray()){
                if( Character.isUpperCase(c)){
                    nameList.add(buffer.toString());
                    buffer.setLength(0);
                    buffer.append(c);
                }else{
                    buffer.append(c);
                }
            }
            nameList.add(buffer.toString());
        } else if(namingStyle.equals(DBName.UPPER_CAMEL_CASE)){
            nameList =new ArrayList<String>();
            StringBuffer buffer=new StringBuffer();
            char[] nameArray=name.toCharArray();
            buffer.append(nameArray[0]);
            for(int i=1;i<nameArray.length;i++){
                if( Character.isUpperCase(nameArray[i])){
                    nameList.add(buffer.toString());
                    buffer.setLength(0);
                    buffer.append(nameArray[i]);
                }else{
                    buffer.append(nameArray[i]);
                }
            }
            nameList.add(buffer.toString());
        } else if(namingStyle.equals(DBName.UNDER_SCORE_CASE)){
            //将名字以 _ 分割为名字数组
            nameList = Arrays.asList(name.split("_"));
        }

    }

    /**
     * 获取名称原始类型
     * @return
     */
    public String getOriginalType(){
        return getOriginalType(0, nameList.size());
    }

    /**
     * 获取名称原始类型
     * @param begin 为开始位置
     * @return
     */
    public String getOriginalType(int begin){
        return getOriginalType(begin, nameList.size());
    }

    /**
     * 获取名称原始类型
     * @param begin 开始位置
     * @param end 结束位置
     * @return
     */
    public String getOriginalType(int begin, int end){
        StringBuffer className=new StringBuffer();
        boolean first=true;
        for(int i=begin;i<end;i++){
            if(first){
                first=false;
            }else{
                className.append("_");
            }
            className.append(nameList.get(i));
        }
        return className.toString();
    }

    /**
     * 获取名称Class类型
     * @return
     */
    public String getClassType(){
        return getClassType(0, nameList.size());
    }

    /**
     * 获取名称Class类型
     * @param begin 为开始位置
     * @return
     */
    public String getClassType(int begin){
        return getClassType(begin, nameList.size());
    }

    /**
     * 获取名称Class类型
     * @param begin 开始位置
     * @param end 结束位置
     * @return
     */
    public String getClassType(int begin, int end){
        StringBuffer className=new StringBuffer();
        for(int i=begin;i<end;i++){
            className.append(nameList.get(i).substring(0,1).toUpperCase()+ nameList.get(i).substring(1).toLowerCase());
        }
        return className.toString();
    }

    /**
     * 获取名称Field类型
     * @return
     */
    public String getFieldType(){
        return getFieldType(0, nameList.size());
    }

    /**
     * 获取名称Field类型
     * @param begin 为开始位置
     * @return
     */
    public String getFieldType(int begin){
        return getFieldType(begin, nameList.size());
    }

    /**
     * 获取名称Field类型
     * @param begin 开始位置
     * @param end 结束位置
     * @return
     */
    public String getFieldType(int begin, int end){
        StringBuffer fieldName=new StringBuffer();
        boolean first=true;
        for(int i=begin;i<end;i++){
            if(first){
                first=false;
                fieldName.append(nameList.get(i).toLowerCase());
            }else{
                fieldName.append(nameList.get(i).substring(0,1).toUpperCase()+ nameList.get(i).substring(1).toLowerCase());
            }
        }
        return fieldName.toString();
    }

    /**
     * 获取名称小写类型
     * @return
     */
    public String getLowerCaseType(){
        return getLowerCaseType(0, nameList.size());
    }

    /**
     * 获取名称小写类型
     * @param begin 为开始位置
     * @return
     */
    public String getLowerCaseType(int begin){
        return getLowerCaseType(begin, nameList.size());
    }

    /**
     * 获取名称小写类型
     * @param begin 开始位置
     * @param end 结束位置
     * @return
     */
    public String getLowerCaseType(int begin, int end){
        StringBuffer fieldName=new StringBuffer();
        boolean first=true;
        for(int i=begin;i<end;i++){
            fieldName.append(nameList.get(i).toLowerCase());
        }
        return fieldName.toString();
    }

    /**
     * 获取名称大写类型
     * @return
     */
    public String getUpperCaseType(){
        return getUpperCaseType(0, nameList.size());
    }

    /**
     * 获取名称大写类型
     * @param begin 为开始位置
     * @return
     */
    public String getUpperCaseType(int begin){
        return getUpperCaseType(begin, nameList.size());
    }

    /**
     * 获取名称大写类型
     * @param begin 开始位置
     * @param end 结束位置
     * @return
     */
    public String getUpperCaseType(int begin, int end){
        StringBuffer fieldName=new StringBuffer();
        boolean first=true;
        for(int i=begin;i<end;i++){
            fieldName.append(nameList.get(i).toUpperCase());
        }
        return fieldName.toString();
    }

}
