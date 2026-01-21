
package com.mk.dts.scheduler.service;

import java.net.InetAddress;

public class NodeMetadataService {

  private String nodeId;
  private String ipAddress;
  private String hostName;
  private long pid;

  public NodeMetadataService(){
    try{
      this.ipAddress = InetAddress.getLocalHost().getHostAddress();
      this.hostName =  InetAddress.getLocalHost().getHostName();
    }catch(Exception e){
      this.ipAddress = "UNKNOWN";
      this.hostName = "UNKNOWN";
    }
     this.pid = ProcessHandle.current().pid();
     this.nodeId = this.hostName + "-" + this.pid;

  }

  public String getNodeId() {
	return nodeId;
  }

  public String getIpAddress() {
	return ipAddress;
  }

  public String getHostName() {
	return hostName;
  }

  public long getPid() {
	return pid;
  }



}
