package com.mk.dts.common.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

 Connection  getConnection() throws SQLException;
}
