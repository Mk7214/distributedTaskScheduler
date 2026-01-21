package com.mk.dts.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mk.dts.common.dao.WorkerMetadataDao;
import com.mk.dts.common.db.DBConnection;
import com.mk.dts.common.model.WorkerMetaData;

public class WorkerMetadataDaoImpl  implements WorkerMetadataDao{
  @Override
  public WorkerMetaData findById(String workerId) throws SQLException {
      WorkerMetaData worker = null;

      String sql = """
          SELECT * FROM worker_metadata WHERE worker_id = ?;
      """;

      try (Connection connection = DBConnection.getConnection();
           PreparedStatement ps = connection.prepareStatement(sql)) {

          ps.setString(1, workerId);

          try (ResultSet rs = ps.executeQuery()) {
              if (rs.next()) {
                  worker = new WorkerMetaData();

                  // Identity
                  worker.setWorkerId(rs.getString("worker_id"));
                  worker.setIpAddress(rs.getString("ip_address"));
                  worker.setHostName(rs.getString("host_name"));
                  worker.setProcessId(rs.getLong("process_id"));

                  // State
                  worker.setStatus(rs.getString("status"));
                  worker.setLogFileName(rs.getString("log_file_name"));

                  // Timestamps
                  worker.setStartedAt(rs.getTimestamp("started_at"));
                  worker.setLastHeartbeat(rs.getTimestamp("last_heartbeat"));
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
          throw e;
      }

      return worker;
  }

  @Override
      public void registerWorker(WorkerMetaData meta) throws SQLException {
          String sql = """
              INSERT INTO worker_metadata (worker_id, ip_address, host_name, process_id, status, log_file_name, last_heartbeat)
              VALUES (?, ?, ?, ?, 'ACTIVE', ?, NOW())
              ON DUPLICATE KEY UPDATE
                  status = 'ACTIVE', last_heartbeat = NOW(), process_id = ?, log_file_name = ?
          """;
          try (Connection conn = DBConnection.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              ps.setString(1, meta.getWorkerId());
              ps.setString(2, meta.getIpAddress());
              ps.setString(3, meta.getHostName());
              ps.setLong(4, meta.getProcessId());
              ps.setString(5, meta.getLogFileName());
              ps.setLong(6, meta.getProcessId());
              ps.setString(7, meta.getLogFileName());
              ps.executeUpdate();
          }
      }

      @Override
      public void updateHeartbeat(String workerId) throws SQLException {
          String sql = "UPDATE worker_metadata SET last_heartbeat = NOW() WHERE worker_id = ?";
          try (Connection conn = DBConnection.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              ps.setString(1, workerId);
              ps.executeUpdate();
          }
      }

      @Override
      public String getStatus(String workerId) throws SQLException {
          String sql = "SELECT status FROM worker_metadata WHERE worker_id = ?";
          try (Connection conn = DBConnection.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql);
              ) {
                ps.setString(1, workerId);
                ResultSet rs = ps.executeQuery();
              if (rs.next()) return rs.getString("status");
          } catch (SQLException e) { e.printStackTrace(); }
          return "UNKNOWN";
      }

      @Override
      public void updateStatus(String workerId, String status) throws SQLException {
          String sql = "UPDATE worker_metadata SET status = ? WHERE worker_id = ?";
          try (Connection conn = DBConnection.getConnection();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              ps.setString(1, status);
              ps.setString(2, workerId);
              ps.executeUpdate();
          }
      }

      @Override
       public List<WorkerMetaData> getAllWorkers() throws SQLException{
         List<WorkerMetaData> meta = new ArrayList<>();
        String sql = "SELECT * FROM worker_metadata";

        try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){
          try(ResultSet rs = ps.executeQuery()){
            while(rs.next()){
              WorkerMetaData data = new WorkerMetaData();
              data.setWorkerId(rs.getString("worker_id"));
              data.setIpAddress(rs.getString("ip_address"));
              data.setHostName(rs.getString("host_name"));
              data.setProcessId(rs.getLong("process_id"));
              data.setStatus(rs.getString("status"));
              data.setLogFileName(rs.getString("log_file_name"));
              data.setStartedAt(rs.getTimestamp("started_at"));
              data.setLastHeartbeat(rs.getTimestamp("last_heartbeat"));
              meta.add(data);
            }
          }
        }
        return meta;
       }
}
