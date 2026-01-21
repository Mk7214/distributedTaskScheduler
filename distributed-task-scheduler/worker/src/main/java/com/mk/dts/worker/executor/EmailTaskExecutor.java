package com.mk.dts.worker.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.mk.dts.common.executor.TaskExecutor;

public class EmailTaskExecutor implements TaskExecutor {

  @Override
  public void execute(JsonNode payload) {

    String to = payload.get("to").asText();
    String subject = payload.get("subject").asText();
    String body = payload.get("body").asText();

    //simulating email sending
      System.out.println("Sending email");
      System.out.println("TO: " + to);
      System.out.println("Subject: " + subject);
      System.out.println("Body + "+ body);

  }

}
