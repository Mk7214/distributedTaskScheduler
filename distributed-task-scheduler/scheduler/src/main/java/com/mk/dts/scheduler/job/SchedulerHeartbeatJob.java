package com.mk.dts.scheduler.job;

import com.mk.dts.common.dao.SchedulerMetadataDao;
import com.mk.dts.common.model.SchedulerMetaData;
import com.mk.dts.scheduler.service.NodeMetadataService;

public class SchedulerHeartbeatJob {

  private final SchedulerMetadataDao metadataDao;
  private final NodeMetadataService nodeInfo;
  private final String logFileName;
  private final String schedulerId;

  public SchedulerHeartbeatJob(SchedulerMetadataDao metadataDao, NodeMetadataService nodeInfo,String logFileName,String schedulerId){
    this.metadataDao = metadataDao;
    this.nodeInfo = nodeInfo;
    this.logFileName = logFileName;
    this.schedulerId = schedulerId;
  }

public void  registerNode(){
  try{
    SchedulerMetaData meta = new SchedulerMetaData();
    meta.setSchedulerId(schedulerId);
    meta.setIpAddress(nodeInfo.getIpAddress());
    meta.setHostName(nodeInfo.getHostName());
    meta.setProcessId(nodeInfo.getPid());
    meta.setRole("STANDBY"); //everyone starts at standby
    meta.setStatus("ACTIVE");
    meta.setLogFileName(this.logFileName);

    metadataDao.registerScheduler(meta);
System.out.println("[INFO] Node registered: " + nodeInfo.getNodeId());
  }catch(Exception e){
System.err.println("[ERROR] Failed to register node: " + e.getMessage());
  }
}

// runs every 30 seconds
public void sendHeartbeat(){
  try{
    metadataDao.updateHeartBeat(nodeInfo.getNodeId());
  }catch(Exception e){
    System.err.println("[WARN] Heartbeat failed: " + e.getMessage());
  }
}

public void checkStatus(){
   try{
     String status = metadataDao.getStatus(nodeInfo.getNodeId());
     if("SHUTDOWN_REQUESTED".equals(status)){
       System.out.println("[ALERT] Shutdown requested ...");
       metadataDao.updateStatus(nodeInfo.getNodeId(), "OFFLINE");
       System.exit(0);
     }
     }catch(Exception e){

     }
}
}
