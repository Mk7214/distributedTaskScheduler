package com.mk.dts.api;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.dts.common.db.TaskDao;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TaskCreateHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange){
    String response = null;
    try{
      if(!exchange.getRequestMethod().equalsIgnoreCase("POST")){
        exchange.sendResponseHeaders(405, -1);
        return;
      }

      InputStream inputStream = exchange.getRequestBody();
      var body = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);

      System.out.println("Recived request body : "  );
      System.out.println(body);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode json = objectMapper.readTree(body);
      String type =  json.get("type").asText();
      int priority =  json.get("priority").asInt();
      //type and priority validation
      if(!json.hasNonNull("type") || !json.hasNonNull("priority")){
        ErrorHandler.sendError(exchange, 400, "Missing required fields");
        return;
      }
      JsonNode payloadNode = json.get("payload");
      //payload validtaion
      if(payloadNode == null || payloadNode.isNull() || payloadNode.isEmpty()){
        ErrorHandler.sendError(exchange, 400, "Payload must not be empty");
        return;
      }

      TaskDao taskDao = new TaskDao();
      taskDao.insertTask(type, payloadNode.toString(), priority);

      response = "Task Submitted successfully\n";
      exchange.sendResponseHeaders(201, response.length());
      exchange.getResponseBody().write(response.getBytes());
      exchange.close();

    }catch(Exception e){
     e.printStackTrace();
    }finally{
      exchange.close();
    }
  }

  }
