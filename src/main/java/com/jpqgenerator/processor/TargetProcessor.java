package com.jpqgenerator.processor;

import com.jpqgenerator.TemplateRender;
import com.jpqgenerator.config.Config;
import com.jpqgenerator.config.ConfigException;
import com.jpqgenerator.db.DBTable;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 目标文件处理
 */
public class TargetProcessor extends Processor {
    private List<DBTable> dbTables;
    private String templateFile;
    private String filePath;
    private String encoding;
    private String targetPackage;
    private String fileName;
    private Map<String,Object> attributeMap;
    private Map<String,Object> globalAttributeMap;
    private Map<String,Object> contextAttributeMap;
    private Map<String,Object> taskAttributeMap;

    public TargetProcessor(List<DBTable> dbTables, Config targetConfig, String filePath, String globalEncoding, Map<String, Object> globalAttributeMap, Map<String, Object> contextAttributeMap, Map<String, Object> taskAttributeMap) throws ConfigException {
        this.dbTables=dbTables;
        this.filePath=filePath;
        this.templateFile=targetConfig.getAttr("template-file");
        this.encoding=targetConfig.getAttrOrDefaultValue("encoding",globalEncoding);
        this.targetPackage=targetConfig.getAttr("target-package");
        this.fileName=targetConfig.getAttr("file-name");
        this.globalAttributeMap=globalAttributeMap;
        this.contextAttributeMap=contextAttributeMap;
        this.taskAttributeMap=taskAttributeMap;
        try {
            this.attributeMap =targetConfig.getPropertyMap();
        }catch (ConfigException e){
            this.attributeMap=null;
        }

    }

    public void process() {
        for(DBTable tmpTable:dbTables){
            getExecutor().execute(new Task(tmpTable));
        }
    }

    public class Task implements Runnable {
        private Map<String, Object> data;
        private TemplateRender templateRender;

        public Task(DBTable dbTable){
            data= new HashMap<String, Object>();
            templateRender = new TemplateRender();
            data.put("table", dbTable);
        }

        @Override
        public void run() {
            try {
                //global
                data.put("global", renderAttributeMap(globalAttributeMap));
                //context
                data.put("context", renderAttributeMap(contextAttributeMap));
                //task
                data.put("task", renderAttributeMap(taskAttributeMap));
                //package
                String tmpTargetPackage = templateRender.renderTemplateString(data, targetPackage);
                data.put("package", tmpTargetPackage);
                String tmpFileName = templateRender.renderTemplateString(data, fileName);
                //attributeMap
                data.put("attributes", renderAttributeMap(attributeMap));
                String tmpFilePath=templateRender.renderTemplateString(data, filePath);
                File directory = new File(tmpFilePath, tmpTargetPackage.replace(".", "\\"));
                directory.mkdirs();
                templateRender.renderTemplateFile(data, new File(templateFile), new File(directory, tmpFileName), encoding);
            }catch (IllegalAccessException e) {
                e.printStackTrace();
                commitError();
                getExecutor().shutdownNow();
            } catch (TemplateException e) {
                e.printStackTrace();
                commitError();
                getExecutor().shutdownNow();
            } catch (IOException e) {
                e.printStackTrace();
                commitError();
                getExecutor().shutdownNow();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                commitError();
                getExecutor().shutdownNow();
            }
        }

        public Map<String,Object> renderAttributeMap(Map<String,Object> attributeMap) throws IOException, TemplateException {
            Map<String, Object> tmpAttributes = new HashMap<String, Object>();
            Object tmpValue=null;
            if(attributeMap!=null){
                for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
                    if(entry.getValue() instanceof String){
                        tmpValue = templateRender.renderTemplateString(data, entry.getValue().toString());
                    }else if(entry.getValue() instanceof List){
                        tmpValue=new ArrayList<String>();
                        for(String tmp:(List<String>)entry.getValue()){
                            ((List)tmpValue).add(templateRender.renderTemplateString(data, tmp));
                        }
                    }
                    tmpAttributes.put(entry.getKey(), tmpValue);
                }
            }
            return tmpAttributes;
        }
    }
}
