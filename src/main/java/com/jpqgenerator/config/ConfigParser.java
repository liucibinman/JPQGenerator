package com.jpqgenerator.config;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * 解析xml
 */
public class ConfigParser {

    /**
     * 解析xml转换为config树
     */
    public static Config getConfig(File configFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document=reader.read(configFile);
        Element rootElement=document.getRootElement();
        Stack<Config.PathNode> path=new Stack<Config.PathNode>();
        return getConfig(rootElement,path);
    }

    /**
     * 解析xml转换为config树
     */
    private static Config getConfig(Element element,Stack<Config.PathNode> path){
        Config confign=new Config((List<Config.PathNode>) path.clone());
        confign.setName(element.getName());
        confign.setValue(element.getTextTrim().equals("")?null:element.getTextTrim());
        Iterator<Attribute> attrIt=element.attributeIterator();
        Attribute tmpAttr;
        Map<String,String> attrs=new HashMap<String,String>();
        while (attrIt.hasNext()){
            tmpAttr = attrIt.next();
            if(tmpAttr.getName().equals("id")){
                confign.setId(tmpAttr.getValue());
            }else{
                attrs.put(tmpAttr.getName(),tmpAttr.getValue());
            }

        }
        if(attrs.size()>0){
            confign.setAttrs(attrs);
        }
        Iterator<Element> elemIt=element.elementIterator();
        List<Config> childConfigs=new ArrayList<Config>();
        path.push(new Config.PathNode(confign.getName(),confign.getId()));
        while (elemIt.hasNext()){
            childConfigs.add(getConfig(elemIt.next(),path));
        }
        if(childConfigs.size()>0){
            confign.setChildConfigs(childConfigs);
        }
        path.pop();
        return confign;
    }
}
