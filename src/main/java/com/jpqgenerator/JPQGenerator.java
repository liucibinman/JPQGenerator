package com.jpqgenerator;

import com.jpqgenerator.config.Config;
import com.jpqgenerator.config.ConfigException;
import com.jpqgenerator.config.ConfigParser;
import com.jpqgenerator.processor.ContextProcessor;
import com.jpqgenerator.processor.Processor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JPQGenerator {

    public JPQGenerator(Config config) throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException, InstantiationException, SQLException, ClassNotFoundException, ExecutionException, InterruptedException, ConfigException {
        //加载jar包
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
        add.setAccessible(true);
        URLClassLoader classloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        for(Config tmpConfig:config.getChildConfigs("jar-path")){
            add.invoke(classloader, new Object[] { new File(tmpConfig.getValue()).toURI().toURL() });
        }

        Config globalAttributes=null;
        Map<String,Object> globalAttributeMap=null;
        try {
            globalAttributes=config.getChildConfig("properties");
        }catch (ConfigException e){

        }
        if(globalAttributes!=null){
            globalAttributeMap=globalAttributes.getPropertyMap();
        }

        ContextProcessor contextProcessor;
        for(Config tmpConfig:config.getChildConfigs("context")){
            contextProcessor=new ContextProcessor(tmpConfig,globalAttributeMap);
            contextProcessor.process();
        }

        Processor.getExecutor().shutdown();
        while (true) {
            if (Processor.getExecutor().isTerminated()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        if (args != null && args.length == 2){
            //生成JPQGenerator项目结构
            try{
                if (args[0].equals("start")){
                    System.out.println("\n");
                    System.out.println("\t　      ┏┓　　　┏┓\n" +
                            "\t　　　┏┛┻━━━┻┗┓\n" +
                            "\t　　　┃　　　　　　　┃\n" +
                            "\t　　　┃　　　━　　　┃\n" +
                            "\t　　　┃　┳┛　┗┳　┃\n" +
                            "\t　　　┃　　　　　　　┃\n" +
                            "\t　　　┃　　　┻　　　┃\n" +
                            "\t　　　┗━┓　 　 ┏━┛\n" +
                            "\t　　　　　┃　 　 ┃\n" +
                            "\t　┏━━━┛　 　 ┃\n" +
                            "\t┏┫　 　 　      ┃\n" +
                            "\t┗┓　 　 　      ┃\n" +
                            "\t　┗┓┏┳━┓┏┏┛\n" +
                            "\t　　┣┣┃　┣┣┃\n" +
                            "\t　　┗┻┛　┗┻┛");
                    System.out.println("\n");
                    Path directory=Paths.get(args[1]);
                    Files.createDirectory(directory);
                    Files.createDirectory(Paths.get(new File(directory.toFile(),"templates").toURI()));
                    Files.copy(JPQGenerator.class.getClassLoader().getResourceAsStream("configuration.xml"), Paths.get(new File(directory.toFile(),"configuration.xml").toURI()));
                    Files.copy(JPQGenerator.class.getClassLoader().getResourceAsStream("jpqgenerator-configuration.dtd"), Paths.get(new File(directory.toFile(),"jpqgenerator-configuration.dtd").toURI()));
                    System.out.println(new Date()+": 生成JPQGenerator项目结构完成!\n");
                    return;
                }
            }catch (FileAlreadyExistsException e){
                System.out.println("\n"+new Date()+": 生成失败,项目文件夹已经存在!\n");
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                System.out.println("\n"+new Date()+": 生成失败!\n");
            }
            return;
        }
        if (args != null && args.length == 1) {
            //生成java项目
            try {
                    System.out.println("\n");
                    System.out.println("\t　      ┏┓　　　┏┓\n" +
                            "\t　　　┏┛┻━━━┻┗┓\n" +
                            "\t　　　┃　　　　　　　┃\n" +
                            "\t　　　┃　　　━　　　┃\n" +
                            "\t　　　┃　┳┛　┗┳　┃\n" +
                            "\t　　　┃　　　　　　　┃\n" +
                            "\t　　　┃　　　┻　　　┃\n" +
                            "\t　　　┗━┓　 　 ┏━┛\n" +
                            "\t　　　　　┃　 　 ┃\n" +
                            "\t　┏━━━┛　 　 ┃\n" +
                            "\t┏┫　 　 　      ┃\n" +
                            "\t┗┓　 　 　      ┃\n" +
                            "\t　┗┓┏┳━┓┏┏┛\n" +
                            "\t　　┣┣┃　┣┣┃\n" +
                            "\t　　┗┻┛　┗┻┛");
                    System.out.println("\n");
                    System.out.println(new Date()+": JPQGenerator正在玩命生成中,请等待...\n");

                    File configFile = new File(args[0]);
                    Config config = ConfigParser.getConfig(configFile);
                    new JPQGenerator(config);

            }catch (Exception e){
                if( Processor.getExecutor()!=null){
                    Processor.getExecutor().shutdownNow();
                }
                //e.printStackTrace();
                System.out.println(e.getMessage());
                Processor.commitError();

            }finally {
                if(Processor.isError()){
                    System.out.println("\n"+new Date()+": 失败!\n");
                }else{
                    System.out.println("\n"+new Date()+": 完成!\n");
                }
            }
        }
    }
}
