package com.mk.dts.scheduler.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzStarter {

  public static void start() throws Exception {

    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    JobDetail job = JobBuilder.newJob(TaskDispatchJob.class)
        .withIdentity("task-dispatch-job", "dts")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("task-dispatch-trigger", "dts")
        .startNow()
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(5)
            .repeatForever())
        .build();

    scheduler.scheduleJob(job, trigger);
    scheduler.start();
    System.out.println("Quartz scheduler start");
  }
}
