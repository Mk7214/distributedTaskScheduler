package com.mk.dts.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.dts.common.dao.WorkerMetadataDao;
import com.mk.dts.common.dao.impl.WorkerMetadataDaoImpl;
import com.mk.dts.common.db.TaskDao;
import com.mk.dts.common.executor.TaskExecutor;
import com.mk.dts.worker.executor.ExecutorRegistry;
import com.mk.dts.worker.job.WorkerHeartbeatJob;
import com.mk.dts.worker.services.NodeMetadataService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class WorkerMain {

    private static final String QUEUE = "NEW_TASK_QUEUE";
    private static final Logger LOGGER = LoggerFactory.getLogger(
        WorkerMain.class
    );

    public static void main(String[] args) throws Exception {

        System.out.println("--- Starting DTS Worker --- ");

        // // setting up hikaricp
        // uncomment this if you get any database issue
        // HikariConfig config = new HikariConfig();
        // config.setJdbcUrl("jdbc:mysql://172.18.0.1:3306/scheduler_db"); // Check your IP
        // config.setUsername("root");
        // config.setPassword("password");
        // config.setMaximumPoolSize(10);
        // HikariDataSource dataSource = new HikariDataSource(config);
        // DBConnection.setProvider(()-> dataSource.getConnection());

        String logFileName = (args.length > 0) ? args[0] : "worker-unkown.log";
        String workerId = (args.length > 1) ? args[1] : "workerUnknown";


        //setting up dependencies
        NodeMetadataService nodeInfo = new NodeMetadataService();
         // database connection pool
        WorkerMetadataDao  metadataDao = new WorkerMetadataDaoImpl();

        System.out.println("==========================================");
        System.out.println(" WORKER STARTED ");
        System.out.println(" NODE ID: " + nodeInfo.getNodeId());  // <--- Writes ID to the top of the file
        System.out.println(" PID    : " + nodeInfo.getPid());
        System.out.println("==========================================");
        System.out.println("[INIT] Identity: " + nodeInfo.getNodeId() + " | Log: " +logFileName);

        //starting heartbeat and monitor
        WorkerHeartbeatJob heartbeat = new WorkerHeartbeatJob(metadataDao, nodeInfo, logFileName,workerId);
        heartbeat.register();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(heartbeat::pulse, 10, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(heartbeat::checkStatus, 5, 10, TimeUnit.SECONDS);

        System.out.println("[INIT] Background Monitor Started.");

        //RabbitMQ Connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.basicQos(1);
        System.out.println("[INFO]Worker is ready waiting for tasks...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            ObjectMapper mapper = new ObjectMapper();
            int id = 0;
            String type = "UNKNOWN";
            JsonNode payload;
            TaskDao dao = new TaskDao();

            try {

                 //updating status to BUSY
                 metadataDao.updateStatus(nodeInfo.getNodeId(), "BUSY");

                 String json = new String(delivery.getBody(), "UTF-8");
                 JsonNode root = mapper.readTree(json);

                 id = root.get("id").asInt();
                 type = root.get("type").asText();
                 payload = root.get("payload");
                 MDC.put("taskId", String.valueOf(id));
                 LOGGER.info(
                    "Received task for processing , ID: " +
                        id +
                        " | type: " +
                        type
                );

                // simulating the work
                Thread.sleep(15000);
                TaskExecutor executor = ExecutorRegistry.getExecutors(type);
                if (executor == null) {
                   LOGGER.error(
                        "No executor found for task type: " , type
                    );
                    channel.basicNack(
                        delivery.getEnvelope().getDeliveryTag(),
                        false,
                        false
                    ); // Don't requeue poison pills
                    return;
                }

                executor.execute(payload);
                // updating the database to mark status as update
                dao.markCompleted(id);
                channel.basicAck(
                    delivery.getEnvelope().getDeliveryTag(),
                    false
                );
                LOGGER.info("Task completed:  " + id);
            }catch(JsonProcessingException jpe){
              LOGGER.error(
                  " [!] FAILED TO PARSE JSON. Check format!"
              );
              channel.basicNack(
                  delivery.getEnvelope().getDeliveryTag(),
                  false,
                  false
              );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Worker interrupted, stopping task ", id);
            } catch (Exception e) {
                try {
                    dao.markFailed(id);
                    channel.basicNack(
                        delivery.getEnvelope().getDeliveryTag(),
                        false,
                        true
                    );
                    LOGGER.info(" Task failed, will retry: " , id);
                } catch (Exception ex) {
                    ex.printStackTrace();
              }
            } finally {
                try{
                  metadataDao.updateStatus(nodeInfo.getNodeId(), "ACTIVE");
                }catch(Exception exception){}
                MDC.clear();
            }
        };

        channel.basicConsume(QUEUE, false, callback, consumerTag -> {});
        // Keep JVM alive
        Thread.currentThread().join();

    }
}
