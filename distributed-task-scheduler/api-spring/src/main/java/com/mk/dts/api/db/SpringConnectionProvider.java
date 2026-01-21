package com.mk.dts.api.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.mk.dts.common.db.ConnectionProvider;
import com.mk.dts.common.db.DBConnection;

@Component
public class SpringConnectionProvider implements ConnectionProvider{

  private final DataSource dataSource;

  public SpringConnectionProvider(DataSource dataSource){
    this.dataSource = dataSource;
    DBConnection.setProvider(this);
  }

  @Override
  public Connection getConnection() throws SQLException{
    return dataSource.getConnection();
  }

}
