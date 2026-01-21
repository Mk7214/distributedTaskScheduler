package com.mk.dts.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

  private static ConnectionProvider provider;

  private static final String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/scheduler_db";
  private static final String user = "root";
  private static final String password = "password";

  static {
          try {
              Class.forName("com.mysql.cj.jdbc.Driver");
          } catch (ClassNotFoundException e) {
              System.err.println("MySQL Driver not found in classpath!");
              e.printStackTrace();
          }
      }

  public static void main(String[] args) {

    try {
      Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
      System.out.println("Connection Successful");
      connection.close();
    } catch (SQLException e) {

      e.printStackTrace();
    }
  }

  public static void setProvider(ConnectionProvider p){
    // if (provider != null) {
    //         throw new IllegalStateException("ConnectionProvider already initialized");
    // }
    provider = p;
  }

  public static Connection getConnection() throws SQLException {
    if(provider != null){
      return provider.getConnection();
    }
    return DriverManager.getConnection(jdbcUrl, user, password);
  }
}
