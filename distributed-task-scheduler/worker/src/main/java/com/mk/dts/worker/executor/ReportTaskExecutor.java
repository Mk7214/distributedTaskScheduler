package com.mk.dts.worker.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.mk.dts.common.executor.TaskExecutor;

public class ReportTaskExecutor  implements TaskExecutor {

    @Override
    public void execute(JsonNode payload){
        String reportName = payload.get("name").asText();

        System.out.println("Generating Report: " + reportName);
    }
}
