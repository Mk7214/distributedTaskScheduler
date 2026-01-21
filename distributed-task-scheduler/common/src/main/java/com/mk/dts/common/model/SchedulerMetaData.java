package com.mk.dts.common.model;

import java.sql.Timestamp;


public class SchedulerMetaData {

  private String schedulerId;
  private String ipAddress;
  private String hostName;
  private long processId;
  private String role; //Leader or STANDBY
  private String status; // Active, shutdown requested ,offline
  private Timestamp lastHeartbeat;
  private Timestamp startedAt;
  private String logFileName;

  public String getSchedulerId() {
	return schedulerId;
  }
  public void setSchedulerId(String schedulerId) {
	this.schedulerId = schedulerId;
  }
  public String getIpAddress() {
	return ipAddress;
  }
  public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
  }
  public String getHostName() {
	return hostName;
  }
  public void setHostName(String hostName) {
	this.hostName = hostName;
  }
  public long getProcessId() {
	return processId;
  }
  public void setProcessId(long processId) {
	this.processId = processId;
  }
  public String getRole() {
	return role;
  }
  public void setRole(String role) {
	this.role = role;
  }
  public String getStatus() {
	return status;
  }
  public void setStatus(String status) {
	this.status = status;
  }
  public Timestamp getLastHeartbeat() {
	return lastHeartbeat;
  }
  public void setLastHeartbeat(Timestamp lastHeartbeat) {
	this.lastHeartbeat = lastHeartbeat;
  }
  public Timestamp getStartedAt() {
	return startedAt;
  }
  public void setStartedAt(Timestamp startedAt) {
	this.startedAt = startedAt;
  }
  public String getLogFileName() {
	return logFileName;
  }
  public void setLogFileName(String logFileName) {
	this.logFileName = logFileName;
  }


}
