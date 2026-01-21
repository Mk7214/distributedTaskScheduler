package com.mk.dts.api.dto;

public class TaskResponse {

  private int id;
  private String type;
  private String payload;
  private int priority;
  private int retryCount;

  public TaskResponse(int id, String type, String payload, int priority, int retryCount){
    this.id = id;
    this.type = type;
    this.payload = payload;
    this.priority = priority;
    this.retryCount = retryCount;
  }

  public int getId() {
	return id;
  }

  public String getType() {
	return type;
  }

  public String getPayload() {
	return payload;
  }

  public int getPriority() {
	return priority;
  }

  public int getRetryCount() {
	return retryCount;
  }
}
