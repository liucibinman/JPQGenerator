package com.jpqgenerator.config;

import java.util.*;

/**
 * xml配置对象
 */
public class Config {

    public static class PathNode{
        String name;
        String id;

        public PathNode() {
        }

        public PathNode(String name) {
            this.name = name;
        }

        public PathNode(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    //节点名称
    private String name;
    //节点名称
    private String id;
    //节点值
    private String value;
    //节属性
    private Map<String,String> attrs;
    //子节点
    private List<Config> childConfigs;

    //从根节点到当前对象的经过的所有节点名字集合,
    private List<PathNode> path;

    public Config(List<PathNode> path){
        this.path=path;
    }

    /**
     * 获取节点名称
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置节点名称
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取节点ID
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置节点ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取节点值
     * @return
     * @throws ConfigException
     */
    public String getValue() throws ConfigException {
        if(value==null){
            throw new ConfigException(this,ConfigException.VALUE_MISSING,null);
        }
        return value;
    }

    /**
     * 获取节点值若为空则使用默认值
     * @param defaultValue
     * @return
     */
    public String getValueOrDefaultValue(String defaultValue) {
        return value==null?defaultValue:value;
    }

    /**
     * 设置节点值
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 通过节点属性名称获取属性值
     * @param attrName
     * @return
     */
    public String getAttr(String attrName) throws ConfigException {
        if(attrs==null){
            throw new ConfigException(this,ConfigException.ATTR_MISSING,attrName);
        }
        if(attrs.get(attrName)==null){
            throw new ConfigException(this,ConfigException.ATTR_MISSING,attrName);
        }
        return attrs.get(attrName);
    }

    /**
     * 通过节点属性名称获取属性值若为空则使用默认值
     * @param attrName
     * @param defaultValue
     * @return
     */
    public String getAttrOrDefaultValue(String attrName,String defaultValue) {
        if(attrs==null){
            return defaultValue;
        }else{
            return attrs.get(attrName)==null?defaultValue:attrs.get(attrName);
        }
    }

    /**
     * 设置节点属性值
     * @param attrName
     * @param attrValue
     */
    public void setAttrs(String attrName,String attrValue) {
        if(attrs==null){
            attrs=new HashMap<String,String>();
        }
        attrs.put(attrName,attrValue);
    }

    /**
     * 获取所有属性map集合
     * @return
     */
    public Map<String, String> getAttrs() throws ConfigException {
        if(attrs==null){
            throw new ConfigException(this,ConfigException.ATTR_MISSING,null);
        }
        return attrs;
    }

    /**
     * 通过map集合设置所有属性，新的map集合将替换原来属性map集合
     * @return
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    /**
     * 获取第一个名字为childConfigName的子节点
     * @return
     */
    public Config getChildConfig(String childConfigName) throws ConfigException {
        if(childConfigs==null){
            throw new ConfigException(this,ConfigException.ELEMENT_MISSING,childConfigName);
        }
        for(Config config:childConfigs){
            if(config.name.equals(childConfigName)){
                return config;
            }
        }
        throw new ConfigException(this,ConfigException.ELEMENT_MISSING,childConfigName);
    }

    /**
     * 获取名字为childConfigName的子节点list集合
     * @param childConfigName
     * @return
     */
    public List<Config> getChildConfigs(String childConfigName) throws ConfigException {
        if(childConfigs==null){
            throw new ConfigException(this,ConfigException.ELEMENT_MISSING,childConfigName);
        }
        List<Config> configs=new ArrayList<Config>();
        for(Config config:childConfigs){
            if(config.name.equals(childConfigName)){
                configs.add(config);
            }
        }
        if(configs.size()==0){
            throw new ConfigException(this,ConfigException.ELEMENT_MISSING,childConfigName);
        }
        return configs;
    }

    /**
     * 获取子节点list集合
     * @return
     */
    public List<Config> getChildConfigs() {
        return childConfigs;
    }

    /**
     * 通过list集合设置所有子节点，新的list集合将替换原来子节点list集合
     * @param childConfigs
     */
    public void setChildConfigs(List<Config> childConfigs) {
        this.childConfigs = childConfigs;
    }


    /**
     * 将该节点的所有property的子节点整合为Map，子节点的name属性和value属性为Map对象的key和value
     * @return
     */
    public Map<String,Object> getPropertyMap() throws ConfigException {

        Map<String,Object> propertyMap=new HashMap<String,Object>();
        List<Config> propertyConfigs=null;

        try{
            propertyConfigs=getChildConfigs("property");
        }catch (ConfigException e){
            throw new ConfigException(this,ConfigException.PROPERTIES_MISSING,null);
        }

        if(propertyConfigs.size()!=childConfigs.size()){
            throw new ConfigException(this,ConfigException.PROPERTIES_ERROR,null);
        }

        List<Config> valueConfigs;
        List<String> values;

        for(Config config:propertyConfigs){

                valueConfigs=null;

                try {
                    valueConfigs = config.getChildConfigs("value");
                }catch (ConfigException e) {

                }

                if(valueConfigs==null) {
                    try {
                        propertyMap.put(config.getAttr("name"), config.getAttr("value"));
                    }catch (ConfigException e){
                        throw new ConfigException(this,ConfigException.PROPERTIES_ERROR,null);
                    }
                }else {

                    if(valueConfigs.size()!=config.getChildConfigs().size()){
                        throw new ConfigException(this,ConfigException.PROPERTY_LIST_ERROR,null);
                    }

                    values=new ArrayList<String>();
                    for (Config valueConfig:valueConfigs){
                        try {
                            values.add(valueConfig.getValue());
                        }catch (ConfigException e){
                            throw new ConfigException(this,ConfigException.PROPERTY_LIST_ERROR,null);
                        }
                    }
                    try {
                        propertyMap.put(config.getAttr("name"),values);
                    }catch (ConfigException e){
                        throw new ConfigException(this,ConfigException.PROPERTY_LIST_ERROR,null);
                    }
                }
        }
        return propertyMap;
    }

    public List<PathNode> getPath() {
        return path;
    }
}
