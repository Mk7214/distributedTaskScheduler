package com.mk.dts.api.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mk.dts.common.db.TaskDao;

@Configuration
public class DaoConfig {

  @Bean
  public TaskDao taskDao(){
    return new TaskDao();
  }

}
