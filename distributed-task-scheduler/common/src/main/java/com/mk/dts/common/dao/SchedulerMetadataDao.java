package com.mk.dts.common.dao;

import java.sql.SQLException;
import java.util.List;

import com.mk.dts.common.model.SchedulerMetaData;

public interface SchedulerMetadataDao {

	void registerScheduler(SchedulerMetaData metaData) throws SQLException;

	void updateHeartBeat(String schedulerId) throws SQLException;

	void updateRole(String schedulerId,String newRole) throws SQLException;

	String getStatus(String schedulerId);

	void updateStatus(String schedulerId, String status) throws SQLException;

	List<SchedulerMetaData> getAllSchedulers() throws SQLException;

	SchedulerMetaData findById(String schedulerId) throws SQLException;

}
