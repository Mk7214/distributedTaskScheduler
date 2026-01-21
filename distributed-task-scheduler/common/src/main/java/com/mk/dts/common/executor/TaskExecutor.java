package com.mk.dts.common.executor;

import com.fasterxml.jackson.databind.JsonNode;

public interface TaskExecutor {

  void execute(JsonNode payload) throws Exception;
}
