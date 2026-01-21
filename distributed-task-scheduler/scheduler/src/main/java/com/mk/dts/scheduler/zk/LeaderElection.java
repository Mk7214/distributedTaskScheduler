package com.mk.dts.scheduler.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import com.mk.dts.common.dao.SchedulerMetadataDao;
import com.mk.dts.scheduler.quartz.QuartzStarter;
import com.mk.dts.scheduler.service.NodeMetadataService;

public class LeaderElection {

  private final LeaderSelector leaderSelector;
  private final SchedulerMetadataDao metadataDao;
  private final NodeMetadataService nodeInfo;

  public LeaderElection(CuratorFramework client, SchedulerMetadataDao metadataDao, NodeMetadataService nodeInfo) {
    this.metadataDao = metadataDao;
    this.nodeInfo = nodeInfo;
    this.leaderSelector = new LeaderSelector(client, "/dts/leader", new LeaderSelectorListenerAdapter() {

      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println("I am the leader");

        //updateing the role to LEADER
        metadataDao.updateRole(nodeInfo.getNodeId(), "LEADER");

        try{

          QuartzStarter.start();

          //keep's holding the leadership
          Thread.currentThread().join();

        }finally{
          //if crash or lost leadership , go  back to standby
          System.out.println("Relinquishing leadership...");
          metadataDao.updateRole(nodeInfo.getNodeId(), "STANDBY");
        }
      }
    });
    // Important: rejoin election if leadership is lost
    this.leaderSelector.autoRequeue();
  }

  public void start() {
    leaderSelector.start();
  }
}
