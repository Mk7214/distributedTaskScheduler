// package com.mk.dts.api.db;


// import javax.sql.DataSource;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;

// @Configuration
// public class DataSourceConfig {
//   @Bean
//   public DataSource dataSource(@Value("${db.url}") String url,
//             @Value("${db.username}") String username,
//             @Value("${db.password}") String password){
//     DriverManagerDataSource ds = new DriverManagerDataSource();
//     ds.setUrl(url);
//     ds.setUsername(username);
//     ds.setPassword(password);
//     return ds;
//   }


// }
