package com.mk.dts.scheduler;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.mk.dts.common.dao.SchedulerMetadataDao;
import com.mk.dts.common.dao.impl.SchedulerMetadataDaoImpl;
import com.mk.dts.common.db.DBConnection;
import com.mk.dts.scheduler.job.SchedulerHeartbeatJob;
import com.mk.dts.scheduler.service.NodeMetadataService;
import com.mk.dts.scheduler.zk.LeaderElection;

public class SchedulerMain {
  private static final String ZK_CONN = "localhost:2181";

  public static void main(String[] args) throws Exception {
    String myLogFileName = null;
    String schedulerid = null;
        if (args.length > 0) {
            myLogFileName = args[0];
            schedulerid = args[1];
            System.out.println("[INIT] Log File Linked: " + myLogFileName);
        } else {
            System.out.println("[WARN] No log filename passed in args. Dashboard won't see logs.");
            myLogFileName = "unknown.log";
        }

    System.out.println("--- Starting the scheduler ---");

    // verifying db connection
    try (Connection connection = DBConnection.getConnection()) {
        System.out.println("Database Connected: "+connection);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    //setting up dependencies
    NodeMetadataService nodeInfo = new NodeMetadataService();
    SchedulerMetadataDao metadataDao = new SchedulerMetadataDaoImpl();



    //setting up heartbeat
    SchedulerHeartbeatJob heartbeatJob = new SchedulerHeartbeatJob(metadataDao, nodeInfo, myLogFileName,schedulerid);

    // registering the node
    heartbeatJob.registerNode();

    //starting the background timer
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //pulse every 30 seconds
    scheduler.scheduleAtFixedRate(heartbeatJob::sendHeartbeat, 10, 30, TimeUnit.SECONDS);

    //check status every 30 seconds
    scheduler.scheduleAtFixedRate(heartbeatJob::checkStatus, 5, 10, TimeUnit.SECONDS);

    System.out.println("[INIT] background heart beat started ");

    //setting up zookeeper
    CuratorFramework zkClient = CuratorFrameworkFactory.newClient(ZK_CONN, new ExponentialBackoffRetry(1000, 3));

    zkClient.start();
    System.out.println("[INIT] Connecting to zoookeeper...");
    if (!zkClient.blockUntilConnected(5, TimeUnit.SECONDS)) {
      System.out.println("zoookeeper not reachable.");
      System.exit(1);
    }

    System.out.println("==========================================");
    System.out.println(" SCHEDULER STARTED ");
    System.out.println(" NODE ID: " + nodeInfo.getNodeId());
    System.out.println(" PID    : " + nodeInfo.getPid());
    System.out.println("==========================================");

    System.out.println("[INFO] Phase 1 complete. Scheduler PID: " + nodeInfo.getPid());

    // start leader election
    LeaderElection leaderElection = new LeaderElection(zkClient,metadataDao,nodeInfo);
    leaderElection.start();

    System.out.println("[INFO] Waiting for leadership...");
    // Keep JVM alive
    Thread.currentThread().join();
  }

}
