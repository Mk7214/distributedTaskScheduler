package com.mk.dts.worker.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.mk.dts.common.executor.TaskExecutor;

public class CleanupExecutor implements TaskExecutor {

    @Override
    public  void execute(JsonNode payload){
        String path = payload.get("path").asText();
        System.out.println("Cleaning up path " + path);
    }
}
