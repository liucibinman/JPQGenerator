package com.jpqgenerator.config;

import java.lang.reflect.Field;

/**
 * 配置异常
 */
public class ConfigException extends Exception{

    public static final int ELEMENT_MISSING =0;
    public static final int ELEMENT_ERROR =1;
    public static final int ATTR_MISSING =2;
    public static final int ATTR_ERROR =3;
    public static final int VALUE_MISSING =4;
    public static final int VALUE_ERROR =5;
    public static final int PROPERTIES_MISSING =6;
    public static final int PROPERTIES_ERROR =7;
    public static final int PROPERTY_LIST_MISSING =8;
    public static final int PROPERTY_LIST_ERROR =9;

    public ConfigException(Config thisConfig,int errorType,String objectName) {
        String describe=null;
        switch (errorType){
            case ELEMENT_MISSING:
                describe=objectName+" 节点对象丢失";
                break;
            case ELEMENT_ERROR:
                describe=objectName+" 节点对象错误";
                break;
            case ATTR_MISSING:
                describe=(objectName==null?"":" "+objectName+" ")+"属性对象丢失";
                break;
            case ATTR_ERROR:
                describe=(objectName==null?"":" "+objectName+" ")+"属性对象错误";
                break;
            case VALUE_MISSING:
                describe="值对象丢失";
                break;
            case VALUE_ERROR:
                describe="值对象错误";
                break;
            case PROPERTIES_MISSING:
                describe="property对象丢失";
                break;
            case PROPERTIES_ERROR:
                describe="property对象错误";
                break;
            case PROPERTY_LIST_MISSING:
                describe="property list对象丢失";
                break;
            case PROPERTY_LIST_ERROR:
                describe="property list对象错误";
                break;
        }
        try {
            Field message=Throwable.class.getDeclaredField("detailMessage");
            message.setAccessible(true);

            StringBuffer pathSB=new StringBuffer();
            for(Config.PathNode node:thisConfig.getPath()){
                pathSB.append(node.getName());
                if(node.getId()!=null){
                    pathSB.append("(id:"+node.getId()+")");
                }
                pathSB.append("节点 -> ");
            }
            pathSB.append(thisConfig.getName());
            if(thisConfig.getId()!=null){
                pathSB.append("(id:"+thisConfig.getId()+")");
            }
            pathSB.append("节点");

            message.set(this,"\n路径: "+pathSB.toString()+"\n信息: "+describe);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
