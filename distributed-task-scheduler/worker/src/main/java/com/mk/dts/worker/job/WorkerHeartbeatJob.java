package com.mk.dts.worker.job;


import com.mk.dts.common.dao.WorkerMetadataDao;
import com.mk.dts.common.model.WorkerMetaData;
import com.mk.dts.worker.services.NodeMetadataService;

public class WorkerHeartbeatJob {

  private final WorkerMetadataDao metadataDao;
  private final NodeMetadataService nodeInfo;
  private final String logFileName;
  private final String workerId;

  public  WorkerHeartbeatJob(WorkerMetadataDao metadataDao, NodeMetadataService nodeInfo, String logFileName, String workerId){
    this.metadataDao = metadataDao;
    this.nodeInfo = nodeInfo;
    this.logFileName = logFileName;
    this.workerId = workerId;
  }

  public void register(){
    try{
      WorkerMetaData meta = new WorkerMetaData();
      meta.setWorkerId(workerId);
      meta.setIpAddress(nodeInfo.getIpAddress());
      meta.setHostName(nodeInfo.getHostName());
      meta.setProcessId(nodeInfo.getPid());
      meta.setLogFileName(logFileName);

      metadataDao.registerWorker(meta);
      System.out.println("[INIT] Worker Registered: " + nodeInfo.getNodeId());
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void pulse(){
    try{
      metadataDao.updateHeartbeat(nodeInfo.getNodeId());
    }catch(Exception e){}
   }
    public void checkStatus() {
      try{
          String status = metadataDao.getStatus(nodeInfo.getNodeId());
          if("SHUTDOWN_REQUESTED".equals(status)){
             System.out.println("[ALERT] Shutdown requested . Stopping worker..");
             metadataDao.updateStatus(nodeInfo.getNodeId(), "OFFLINE");;
             System.exit(0);
        }
      }catch(Exception e){ }
    }
 }
