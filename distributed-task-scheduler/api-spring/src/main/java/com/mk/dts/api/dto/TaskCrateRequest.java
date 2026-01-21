package com.mk.dts.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskCrateRequest {


  @NotBlank(message = "Task type is required")
  private String type;

  @NotNull(message = "payload must not be null")
  private Object payload;

  @Min(value = 1, message = "Priority must be >= 1")
  private int priority;

  public String getType() {
	return type;
  }
  public void setType(String type) {
	this.type = type;
  }
  public Object getPayload() {
	return payload;
  }
  public void setPayload(Object payload) {
	this.payload = payload;
  }
  public int getPriority() {
	return priority;
  }
  public void setPriority(int priority) {
	this.priority = priority;
  }


}
