package com.mk.dts.common.model;

import java.sql.Timestamp;

public class Task {

    private int id;
    private String taskType;
    private String payload;
    private int priority;
    private String Status;
    private int retryCount;
    private Timestamp scheduledAt;

    public int getRetryCount() {
        return retryCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getPayload() {
        return payload;
    }

    public int getPriority() {
        return priority;
    }

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public Timestamp getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(Timestamp scheduledAt) {
		this.scheduledAt = scheduledAt;
	}
}
