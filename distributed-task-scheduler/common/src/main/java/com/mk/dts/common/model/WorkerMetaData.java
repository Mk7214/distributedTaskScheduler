package com.mk.dts.common.model;

import java.sql.Timestamp;

public class WorkerMetaData {

 private String workerId;
 private String ipAddress;
 private String hostName;
 private long processId;
 private String status;
 private String logFileName;
 private Timestamp startedAt;
 private Timestamp lastHeartbeat;


 public String getWorkerId() {
	return workerId;
 }
 public void setWorkerId(String workerId) {
	this.workerId = workerId;
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
 public String getStatus() {
	return status;
 }
 public void setStatus(String status) {
	this.status = status;
 }
 public String getLogFileName() {
	return logFileName;
 }
 public void setLogFileName(String logFileName) {
	this.logFileName = logFileName;
 }
 public Timestamp getStartedAt() {
	return startedAt;
 }
 public void setStartedAt(Timestamp startedAt) {
	this.startedAt = startedAt;
 }
 public Timestamp getLastHeartbeat() {
	return lastHeartbeat;
 }
 public void setLastHeartbeat(Timestamp lastHeartbeat) {
	this.lastHeartbeat = lastHeartbeat;
 }

}
