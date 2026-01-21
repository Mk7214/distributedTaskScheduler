package com.mk.dts.scheduler.quartz;

import com.mk.dts.common.db.TaskDao;
import com.mk.dts.common.model.Task;
import com.mk.dts.scheduler.queue.TaskPublisher;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskDispatchJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        TaskDispatchJob.class
    );

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        LOGGER.info("quartz tick: checking for tasks...");
        TaskDao taskDao = new TaskDao();

        try {
            List<Task> tasks = taskDao.fetchPendingTasks();
            LOGGER.info("Fetched tasks count = ", tasks.size());

            for (Task task : tasks) {
                LOGGER.info("About to mark RUNNING for task ", task.getId());

                if (taskDao.markRunning(task.getId())) TaskPublisher.publish(
                    task.getId(),
                    task.getTaskType(),
                    task.getPayload()
                );
                else return;
            }

            // Hung task recovery.
            List<Task> hungTasks = taskDao.findHungTasks();

            for (Task task : hungTasks) {
                if (task.getRetryCount() + 1 >= 3) {
                    taskDao.moveToDeadQueue(
                        task,
                        "Retries exhausted (hung task)"
                    );
                } else {
                    taskDao.recoverHungTask(task.getId());
                }
            }

            int cleaned = taskDao.cleanupExhaustedRunningTasks();
            if (cleaned > 0) {
                LOGGER.warn(
                    "Marked {} exhausted RUNNING tasks as FAILED",
                    cleaned
                );
            }

            if (tasks.isEmpty()) {
                System.out.println("No Pending tasks");
            }
        } catch (Exception e) {
            LOGGER.error("Scheduler execution failed", e);
        }
    }
}
