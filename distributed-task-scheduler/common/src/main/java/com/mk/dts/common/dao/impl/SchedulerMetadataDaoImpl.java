package com.mk.dts.common.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mk.dts.common.dao.SchedulerMetadataDao;
import com.mk.dts.common.db.DBConnection;
import com.mk.dts.common.model.SchedulerMetaData;

public class SchedulerMetadataDaoImpl implements SchedulerMetadataDao {
  @Override
  public SchedulerMetaData findById(String schedulerId) throws SQLException {
      SchedulerMetaData meta = null;

      String sql = """
          SELECT * FROM scheduler_metadata WHERE scheduler_id = ?;
      """;

      try (Connection connection = DBConnection.getConnection();
           PreparedStatement ps = connection.prepareStatement(sql)) {

          // FIX: Use the method argument 'schedulerId', not the uninitialized 'meta' object
          ps.setString(1, schedulerId);

          try (ResultSet rs = ps.executeQuery()) {
              if (rs.next()) {
                  meta = new SchedulerMetaData();

                  // Mapping DB columns (snake_case) to Java fields (camelCase)
                  meta.setSchedulerId(rs.getString("scheduler_id"));
                  meta.setIpAddress(rs.getString("ip_address"));
                  meta.setHostName(rs.getString("host_name"));
                  meta.setProcessId(rs.getLong("process_id"));
                  meta.setRole(rs.getString("role"));
                  meta.setStatus(rs.getString("status"));

                  // Timestamps
                  meta.setLastHeartbeat(rs.getTimestamp("last_heartbeat"));
                  meta.setStartedAt(rs.getTimestamp("started_at"));

                  // Log file
                  meta.setLogFileName(rs.getString("log_file_name"));
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
          throw e; // Since your method signature says 'throws SQLException', it's best to re-throw it
      }

      return meta; // Returns the object if found, or null if not found
  }
@Override
    public void registerScheduler(SchedulerMetaData meta) throws SQLException {
        String sql = """
            INSERT INTO scheduler_metadata (scheduler_id, ip_address, host_name, process_id, role, status, last_heartbeat, started_at,log_file_name)
            VALUES (?, ?, ?, ?, ?, 'ACTIVE', NOW(), NOW(),?)
            ON DUPLICATE KEY UPDATE
                status = 'ACTIVE', last_heartbeat = NOW(), process_id = ?
        """;
        try(Connection connection = DBConnection.getConnection();
          PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, meta.getSchedulerId());
            ps.setString(2,meta.getIpAddress());
            ps.setString(3, meta.getHostName());
            ps.setLong(4,meta.getProcessId());
            ps.setString(5, meta.getRole());
            ps.setString(6,meta.getLogFileName());
            ps.setLong(7, meta.getProcessId());
            ps.executeUpdate();
        }
    }

@Override
public void updateHeartBeat(String schedulerId) throws SQLException {
        String sql = "UPDATE scheduler_metadata SET last_heartbeat = NOW() WHERE scheduler_id = ?";
        try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){
             ps.setString(1, schedulerId);
             ps.executeUpdate();

          }
    }

    @Override
    public void updateRole(String schedulerId, String newRole) throws SQLException{
        String sql = "UPDATE scheduler_metadata SET role = ? WHERE scheduler_id = ?";
        try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){
             ps.setString(1, newRole);
             ps.setString(2, schedulerId);
             ps.executeUpdate();

          }
    }

    @Override
    public String getStatus(String schedulerId) {
        String sql = "SELECT status FROM scheduler_metadata WHERE scheduler_id = ?";
        try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setString(1, schedulerId);
             try(ResultSet rs = ps.executeQuery()){
              if(rs.next()){
                return rs.getString("status");
              }
          }
        }catch(SQLException e){
          e.printStackTrace();
        }
          return "UNKNOWN";
    }

    @Override
    public void updateStatus(String schedulerId, String status)  throws SQLException{
        String sql = "UPDATE scheduler_metadata SET status = ? WHERE scheduler_id = ?";
        try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){
          ps.setString(1, status);
          ps.setString(2, schedulerId);
          ps.executeUpdate();
        }
    }

    @Override
    public List<SchedulerMetaData> getAllSchedulers() throws SQLException{
      List<SchedulerMetaData> meta = new ArrayList<>();
      String sql = "SELECT * FROM scheduler_metadata";
      try(Connection connection = DBConnection.getConnection();             PreparedStatement ps = connection.prepareStatement(sql)){

        try(ResultSet rs = ps.executeQuery()){
          while(rs.next()){
            SchedulerMetaData data = new SchedulerMetaData();
            data.setSchedulerId(rs.getString("scheduler_id"));
            data.setIpAddress(rs.getString("ip_address"));
            data.setHostName(rs.getString("host_name"));
            data.setProcessId(rs.getLong("process_id"));
            data.setRole(rs.getString("role"));
            data.setStatus(rs.getString("status"));
            data.setStartedAt(rs.getTimestamp("started_at"));
            data.setLastHeartbeat(rs.getTimestamp("last_heartbeat"));
            meta.add(data);
          }
        }
      }
      return meta;
    }

}
