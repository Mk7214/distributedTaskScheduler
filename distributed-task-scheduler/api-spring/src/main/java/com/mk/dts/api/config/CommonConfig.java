package com.mk.dts.api.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.mk.dts.common.dao.SchedulerMetadataDao;
import com.mk.dts.common.dao.WorkerMetadataDao;
import com.mk.dts.common.dao.impl.SchedulerMetadataDaoImpl;
import com.mk.dts.common.dao.impl.WorkerMetadataDaoImpl;
import com.mk.dts.common.db.DBConnection;

import jakarta.annotation.PostConstruct;

@Configuration
public class CommonConfig {

  private final DataSource dataSource;

  //spring injects the datasource(Hikaricp)
  public CommonConfig(DataSource datasource){
    this.dataSource = datasource;
  }


  @PostConstruct
  public void setupLegacyDBConnection() {
      try {
          // We use a lambda to wrap the DataSource.getConnection()
          DBConnection.setProvider(() -> dataSource.getConnection());
          System.out.println("[CONFIG] Successfully bridged Spring DataSource to DBConnection.");
      } catch (IllegalStateException e) {
          System.out.println("[CONFIG] DBConnection provider already set. Skipping.");
      }
  }


  //creating a daobean manually;
  @Bean
  public SchedulerMetadataDao schedulerMetadataDao(){
    return new SchedulerMetadataDaoImpl();
  }

  @Bean
  public WorkerMetadataDao workerMetadataDao(){
    return new WorkerMetadataDaoImpl();
  }

}
