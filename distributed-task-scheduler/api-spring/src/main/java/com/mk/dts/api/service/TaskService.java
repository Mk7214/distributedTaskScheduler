package com.mk.dts.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mk.dts.api.dto.DeadTaskResponse;
import com.mk.dts.api.dto.TaskResponse;
import com.mk.dts.common.db.TaskDao;
import com.mk.dts.common.model.DeadTask;
import com.mk.dts.common.model.Task;

@Service
public class TaskService {
  private final TaskDao dao;

  public TaskService(TaskDao taskDao){
    this.dao = taskDao;
  }

  //need to handle the exception correctly
  public void createTask(String type, String payload, int priority) throws Exception{
    dao.insertTask(type, payload, priority);
  }

  public List<TaskResponse> getTasks(int page, int size) throws Exception{

       int offset = page * size;
       List<Task> tasks = dao.fetchCompletedTask(size,offset);

    return tasks.stream()
                 .map(task -> new TaskResponse(
                   task.getId(),
                   task.getTaskType(),
                   task.getPayload(),
                   task.getPriority(),
                   task.getRetryCount()))
                 .toList();

  }

  //todo: need to handle empty tasks
  public List<TaskResponse> getCompletedTasks(int page, int size) throws Exception{

       int offset = page * size;
       List<Task> tasks = dao.fetchCompletedTask(size,offset);

    return tasks.stream()
                 .map(task -> new TaskResponse(
                   task.getId(),
                   task.getTaskType(),
                   task.getPayload(),
                   task.getPriority(),
                   task.getRetryCount()))
                 .toList();

  }

  //todo: need to handle empty dead tasks
  public List<DeadTaskResponse> getDeadTasks(int page, int size) throws Exception{
     int offset = page * size;
    List<DeadTask> deadTasks = dao.fetchDeadTasks(size,offset );

    return deadTasks.stream()
                   .map(task -> new DeadTaskResponse(
                     task.getId(),
                     task.getTaskId(),
                     task.getTaskType(),
                     task.getPayload(),
                     task.getPriority(),
                     task.getFailureReason(),
                     task.getFailedAt()))
                   .toList();
  }

}
