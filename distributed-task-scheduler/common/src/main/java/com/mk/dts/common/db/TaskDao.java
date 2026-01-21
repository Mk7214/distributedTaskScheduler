package com.mk.dts.common.db;

import com.mk.dts.common.model.DeadTask;
import com.mk.dts.common.model.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public List<Task> fetchTasks(int limit, int offset) throws Exception{

      List<Task> tasks = new ArrayList<>();
      String sql = """
         SELECT id, task_type, payload, priority, retry_count, started_at
         FROM tasks
         ORDER BY id DESC
         LIMIT ? OFFSET ?
      """;
      try (
                  Connection connection = DBConnection.getConnection();
                  PreparedStatement preparedStatement = connection.prepareStatement(
                      sql
                  )) {
                preparedStatement.setInt(1, limit);
                preparedStatement.setInt(2,offset);

               try(ResultSet resultSet = preparedStatement.executeQuery()){

                 while (resultSet.next()) {
                     Task task = new Task();
                     task.setId(resultSet.getInt("id"));
                     task.setTaskType(resultSet.getString("task_type"));
                     task.setPayload(resultSet.getString("payload"));
                     task.setPriority(resultSet.getInt("priority"));
                     tasks.add(task);
                 }
               }
              }

     return tasks;

    }

    public List<DeadTask> fetchDeadTasks(int limit, int offset) throws Exception{
     List<DeadTask> deadTasks = new ArrayList<>();
     String sql = """
        SELECT id,taskId, task_type, payload, failure_reason, failed_at, priority
        FROM dead_tasks
        ORDER BY id DESC
        LIMIT ? OFFSET ?
     """;
     try (
                 Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(
                     sql
                 );
                              ) {

               preparedStatement.setInt(1, limit);
               preparedStatement.setInt(2,offset);
               try(ResultSet resultSet = preparedStatement.executeQuery()){
                 while (resultSet.next()) {
                                     DeadTask task = new DeadTask();
                                     task.setId(resultSet.getInt("id"));
                                     task.setTaskId(resultSet.getInt("taskId"));
                                     task.setTaskType(resultSet.getString("task_type"));
                                     task.setPayload(resultSet.getString("payload"));
                                     task.setFailureReason(resultSet.getString("failure_reason"));
                                     task.setFailedAt(resultSet.getTimestamp("failed_at"));
                                     task.setPriority(resultSet.getInt("priority"));
                                     deadTasks.add(task);
                                 }
                             }
              }
    return deadTasks;
    }

    public List<Task> fetchCompletedTask(int limit, int offset ) throws Exception{

      List<Task> completedTasks = new ArrayList<>();
      String sql = """
         SELECT id, task_type, payload, priority, retry_count, started_at
         FROM tasks
         WHERE status = 'COMPLETED'
         ORDER BY id DESC
         LIMIT ? OFFSET ?
      """;
      try (
                  Connection connection = DBConnection.getConnection();
                  PreparedStatement preparedStatement = connection.prepareStatement(
                      sql
                  )) {
                preparedStatement.setInt(1, limit);
                preparedStatement.setInt(2,offset);

               try(ResultSet resultSet = preparedStatement.executeQuery()){

                 while (resultSet.next()) {
                     Task task = new Task();
                     task.setId(resultSet.getInt("id"));
                     task.setTaskType(resultSet.getString("task_type"));
                     task.setPayload(resultSet.getString("payload"));
                     task.setPriority(resultSet.getInt("priority"));
                     completedTasks.add(task);
                 }
               }
              }

     return completedTasks;
    }

    public List<Task> findHungTasks() throws Exception {
        List<Task> hungTasks = new ArrayList<>();

        String sql = """
                SELECT id, task_type, payload, priority
                FROM tasks
                WHERE status = 'RUNNING'
                  AND retry_count < 3
                  AND started_at IS NOT NUll
                  AND started_at < (NOW() - INTERVAL timeout_seconds SECOND)
            """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            );
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTaskType(resultSet.getString("task_type"));
                task.setPayload(resultSet.getString("payload"));
                task.setPriority(resultSet.getInt("priority"));
                hungTasks.add(task);
            }
        }
        return hungTasks;
    }

    public List<Task> fetchPendingTasks() throws Exception {
        List<Task> tasks = new ArrayList<>();

        String sql = """
            SELECT id, task_type, payload, priority
            FROM tasks
            WHERE status = 'PENDING'
                  AND retry_count < 3
                 AND (scheduled_at IS NULL OR scheduled_at <= NOW())
             ORDER BY priority ASC
            """;
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            );
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTaskType(resultSet.getString("task_type"));
                task.setPayload(resultSet.getString("payload"));
                task.setPriority(resultSet.getInt("priority"));
                tasks.add(task);
            }
        }

        return tasks;
    }

    public boolean markRunning(int taskId) throws Exception {
        String sql = """
                UPDATE tasks
                SET status = 'RUNNING',
                  started_at = NOW()
                 WHERE id = ?
                 AND status = 'PENDING'
                 AND retry_count < 3
            """;
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            preparedStatement.setInt(1, taskId);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public void markCompleted(int taskId) throws Exception {
        String sql = "UPDATE tasks SET status = 'COMPLETED' WHERE id = ?";
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        }
    }

    public void markFailed(int taskId) throws Exception {
        String sql = """
                UPDATE tasks
                SET retry_count = retry_count + 1,
                 status = CASE
                      WHEN retry_count >= 2 THEN 'FAILED'
                      ELSE 'PENDING'
                 END
                WHERE id = ?
            """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        }
    }

    public void recoverHungTask(int taskId) throws Exception {
        String sql = """
            UPDATE tasks
            SET retry_count = retry_count + 1,
                status = CASE
                    WHEN retry_count + 1 >= 3 THEN 'FAILED'
                    ELSE 'PENDING'
                END,
                started_at = NULL
            WHERE id = ?
              AND status = 'RUNNING'
              AND retry_count < 3
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        }
    }

    public void resetStuckTasks() throws Exception {
        String sql =
            "UPDATE tasks SET status = 'PENDING' WHERE status = 'RUNNING'";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println(
                    ">>> System Recovery: Moved " +
                        rows +
                        " stuck tasks back to PENDING"
                );
            }
        }
    }

    public int cleanupExhaustedRunningTasks() throws Exception {
        String sql = """
                  UPDATE tasks
            SET status = 'FAILED'
            WHERE status = 'RUNNING'
              AND retry_count >= 3
              AND NOW() > (started_at + INTERVAL timeout_seconds SECOND);
             """;
        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql
            )
        ) {
            return preparedStatement.executeUpdate();
        }
    }

    public void moveToDeadQueue(Task task, String reason) throws Exception {
        String insertSql = """
                INSERT INTO
                  dead_tasks (original_task_id, task_type, payload, failure_reason)
                 VALUES (?, ?, ?, ?)
            """;
        String deleteSql = "DELETE FROM tasks where id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (
                PreparedStatement insert = connection.prepareStatement(
                    insertSql
                );
                PreparedStatement delete = connection.prepareStatement(
                    deleteSql
                )
            ) {
                insert.setInt(1, task.getId());
                insert.setString(2, task.getTaskType());
                insert.setString(3, task.getPayload());
                insert.setString(4, reason);
                insert.executeUpdate();

                delete.setInt(1, task.getId());
                delete.executeUpdate();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

public void insertTask(String type, String payload, int priority) throws Exception{
  String sql = """
       INSERT INTO tasks (
       task_type, payload, priority, status, retry_count, scheduled_at, timeout_seconds
       )
       VALUES (?,?,?,'PENDING',0,NOW(),30)

  """;
  try(Connection connection = DBConnection.getConnection();
    PreparedStatement preparedStatement =connection.prepareStatement(sql)){
      preparedStatement.setString(1, type);
      preparedStatement.setString(2, payload);
      preparedStatement.setInt(3, priority);

      preparedStatement.executeUpdate();
    }}
}
