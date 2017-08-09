package com.jpqgenerator;

import freemarker.cache.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;

/**
 * 模板渲染器
 */
public class TemplateRender {
    private  Configuration configuration;
    private  StringTemplateLoader stringTemplateLoader;

    public TemplateRender() {
        configuration=new Configuration(Configuration.VERSION_2_3_26);
        stringTemplateLoader =new StringTemplateLoader();
    }

    /**
     * 渲染字符串模板
     * @param dataModel
     * @param templateStr
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String renderTemplateString(Object dataModel, String templateStr) throws IOException, TemplateException {
        stringTemplateLoader.putTemplate(String.valueOf(templateStr.hashCode()),templateStr);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template=configuration.getTemplate(String.valueOf(templateStr.hashCode()));
        Writer out = new StringWriter();
        template.process(dataModel,out);
        return out.toString();
    }

    /**
     * 渲染字符串模板
     * @param dataModel
     * @param templateStr
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public void renderTemplateString(Object dataModel, String templateStr, File outPath) throws IOException, TemplateException {
        stringTemplateLoader.putTemplate(String.valueOf(templateStr.hashCode()),templateStr);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template=configuration.getTemplate(String.valueOf(templateStr.hashCode()));
        Writer out = new FileWriter(outPath);
        template.process(dataModel,out);
    }

    /**
     * 渲染文件模板
     * @param dataModel
     * @param templateFile
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String renderTemplateFile(Object dataModel,File templateFile) throws IOException, TemplateException {
        if(templateFile.isAbsolute()){
            configuration.setTemplateLoader(new FileTemplateLoader(templateFile.getParentFile()));
        }else{
            configuration.setTemplateLoader(new FileTemplateLoader(new File(System.getProperty("user.dir"))));
        }
        Template template=configuration.getTemplate(templateFile.getName());
        Writer out = new StringWriter();
        template.process(dataModel,out);
        return out.toString();
    }

    /**
     * 渲染文件模板
     * @param dataModel
     * @param templateFile
     * @param outPath
     * @param encoding
     * @throws IOException
     * @throws TemplateException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void renderTemplateFile(Object dataModel, File templateFile, File outPath, String encoding) throws IOException, TemplateException, NoSuchFieldException, IllegalAccessException {
        if(templateFile.isAbsolute()){
            configuration.setTemplateLoader(new FileTemplateLoader(templateFile.getParentFile()));
        }else{
            configuration.setTemplateLoader(new FileTemplateLoader(new File(System.getProperty("user.dir"))));
        }
        Template template=configuration.getTemplate(templateFile.getPath(), encoding);
        Writer out = new OutputStreamWriter(new FileOutputStream(outPath),encoding);
        template.process(dataModel,out);
        out.flush();
    }

}
