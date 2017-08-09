package com.jpqgenerator.processor;

import com.jpqgenerator.config.Config;
import com.jpqgenerator.config.ConfigException;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 上下文处理
 */
public class ContextProcessor extends Processor {
    private Map<String,Object> jdbcConnectionPropertyMap;
    private List<Config> taskConfigs;
    private Map<String,Object> globalAttributeMap;
    private Map<String,Object> contextAttributeMap;

    public ContextProcessor(Config contextConfig,Map<String,Object> globalAttributeMap) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, ConfigException {
        this.jdbcConnectionPropertyMap=contextConfig.getChildConfig("jdbc-connection").getPropertyMap();
        this.taskConfigs=contextConfig.getChildConfigs("task");
        this.globalAttributeMap=globalAttributeMap;
        Config contextAttributes=null;
        try {
            contextAttributes=contextConfig.getChildConfig("properties");
        }catch (ConfigException e){

        }
        if(contextAttributes!=null){
            contextAttributeMap=contextAttributes.getPropertyMap();
        }
    }

    public void process() throws ClassNotFoundException, SQLException, ExecutionException, InterruptedException {
        Class.forName(jdbcConnectionPropertyMap.get("jdbcDriver").toString());
        List<Future<Boolean>> futures=new ArrayList<Future<Boolean>>();
        for(Config tmpConfig:taskConfigs){
            futures.add(getExecutor().submit(new Task(tmpConfig)));
        }
        for(Future<Boolean> tmpFuture:futures){
            tmpFuture.get();
        }
    }

    public class Task implements Callable<Boolean>{
        private Config taskConfig;

        public Task(Config taskConfig){
            this.taskConfig=taskConfig;
        }

        @Override
        public Boolean call() throws SQLException, ConfigException, IllegalAccessException, InterruptedException, ExecutionException, InstantiationException, MalformedURLException, ClassNotFoundException {
            Properties tmpProperties=new Properties();
            for(Map.Entry entry:jdbcConnectionPropertyMap.entrySet()){
                if(entry.getValue() instanceof String){
                    tmpProperties.setProperty(entry.getKey().toString(),entry.getValue().toString());
                }
            }
            Connection connection= DriverManager.getConnection(jdbcConnectionPropertyMap.get("jdbcUrl").toString(),tmpProperties);
            TaskProcesser taskProcesser=new TaskProcesser(taskConfig,connection,globalAttributeMap,contextAttributeMap);
            taskProcesser.process();
            connection.close();
            return true;
        }
    }
}
