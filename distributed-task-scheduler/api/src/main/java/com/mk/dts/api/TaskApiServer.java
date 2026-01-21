package com.mk.dts.api;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class TaskApiServer {

  public static void main(String[] args) {
    try{
    HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
      server.createContext("/tasks", new TaskCreateHandler());
      server.createContext("/completed", new CompletedTaskHandler());
      server.createContext("/deadTasks", new DeadTaskHandler());
      server.setExecutor(null);
      server.start();
      System.out.println("Task Api is started at port 8080");
    }catch(Exception e){
      System.err.println("Error creating a server");
    }
  }

}
