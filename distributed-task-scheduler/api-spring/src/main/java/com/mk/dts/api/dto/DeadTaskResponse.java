
package com.mk.dts.api.dto;

import java.sql.Timestamp;

public class DeadTaskResponse {

  private int id;
  private int taskId;
  private String type;
  private String payload;
  private int priority;
  private String failureReason;
  private Timestamp failedAt;

  public DeadTaskResponse(int id, int taskId, String type, String payload, int priority,String failureReason, Timestamp failedAt){
    this.id = id;
    this.taskId = taskId;
    this.type = type;
    this.payload = payload;
    this.priority = priority;
    this.failureReason = failureReason;
    this.failedAt = failedAt;
  }

  public int getId() {
	return id;
  }

  public int getTaskId() {
	return taskId;
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

  public String getFailureReason() {
	return failureReason;
  }

  public Timestamp getFailedAt() {
	return failedAt;
  }


}
