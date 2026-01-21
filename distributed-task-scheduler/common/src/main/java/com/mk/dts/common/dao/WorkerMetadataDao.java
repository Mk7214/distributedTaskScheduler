package com.mk.dts.common.dao;

import java.sql.SQLException;
import java.util.List;

import com.mk.dts.common.model.WorkerMetaData;

public interface WorkerMetadataDao {

  void registerWorker(WorkerMetaData meta) throws SQLException;

  void updateHeartbeat(String workerId) throws SQLException;

  String getStatus(String workerId) throws SQLException;

  void updateStatus(String workerId,String status) throws SQLException;

  List<WorkerMetaData> getAllWorkers() throws SQLException;

  WorkerMetaData findById(String workerId) throws SQLException;


}
