package com.jpqgenerator.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理器
 */
public abstract class Processor {
    private static volatile ExecutorService executor=null;
    private static volatile boolean error;

    static {
        executor = Executors.newCachedThreadPool();
        error = false;
    }

    public static ExecutorService getExecutor(){
        return executor;
    }

    public synchronized  static void commitError() {
        error = true;
    }

    public static boolean isError(){
        return error;
    }

}
