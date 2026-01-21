package com.mk.dts.worker.executor;

import com.mk.dts.common.executor.TaskExecutor;

import java.util.HashMap;
import java.util.Map;

public class ExecutorRegistry {

    private static final Map<String, TaskExecutor> EXECUTORS = new HashMap<>();

    static {
        EXECUTORS.put("EMAIL", new EmailTaskExecutor());
        EXECUTORS.put("REPORT",new ReportTaskExecutor());
        EXECUTORS.put("CLEANUP", new CleanupExecutor());
    }

    public static  TaskExecutor getExecutors(String taskType){
        return EXECUTORS.get(taskType);
    }
}
