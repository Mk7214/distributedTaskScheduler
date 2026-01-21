package com.mk.dts.common.model;

import java.sql.Timestamp;

public class DeadTask {

    private int id;
    private int taskId;
    private String taskType;
    private String payload;
    private int priority;
    private String failureReason;
    private Timestamp failedAt;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public Timestamp getFailedAt() {
		return failedAt;
	}
	public void setFailedAt(Timestamp failedAt) {
		this.failedAt = failedAt;
	}


}
