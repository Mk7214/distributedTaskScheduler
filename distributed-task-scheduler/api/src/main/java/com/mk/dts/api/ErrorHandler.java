package com.mk.dts.api;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;

public class ErrorHandler {

  public static void sendError(HttpExchange exchange, int statusCode, String message){
    try{
      byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(statusCode, bytes.length);
      try(OutputStream os = exchange.getResponseBody()){
        os.write(bytes);
        ErrorHandler.sendError(exchange, 500, "Internal Server Error");
      }
    }catch(Exception exception){
      exception.printStackTrace();
    }
  }
}
