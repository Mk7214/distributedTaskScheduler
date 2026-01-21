package com.mk.dts.api;

import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.dts.common.db.TaskDao;
import com.mk.dts.common.model.DeadTask;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class DeadTaskHandler implements HttpHandler {

  private final  ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void handle(HttpExchange exchange){
   try{
     if(!exchange.getRequestMethod().equalsIgnoreCase("GET")){
       exchange.sendResponseHeaders(405, -1);
       return;
     }

     TaskDao taskDao = new TaskDao();
     List<DeadTask> deadTasks = taskDao.fetchDeadTasks(10, 0);
     System.out.println(deadTasks);
     byte[] response =  MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(deadTasks);

     exchange.getResponseHeaders().add("Content-Type", "application/json");
     exchange.sendResponseHeaders(200, response.length);

    try(OutputStream outputStream = exchange.getResponseBody()){
      outputStream.write(response);
    }

   }catch(Exception e){
     e.printStackTrace();
     ErrorHandler.sendError(exchange, 500, "Internal Server Error");

   }finally{
     exchange.close();
   }
  }
}
