package com.mk.dts.scheduler.queue;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TaskPublisher {

  private static final String QUEUE = "NEW_TASK_QUEUE";
  private static Connection connection = null;
  private static Channel channel = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(TaskPublisher.class);

  // Synchronized to ensure only one connection is created in multi-threaded
  // Quartz
  private static synchronized void init() throws Exception {
    if (connection == null || !connection.isOpen()) {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("172.18.0.1");
      connection = factory.newConnection();
      channel = connection.createChannel();
      channel.queueDeclare(QUEUE, true, false, false, null);
      System.out.println(">>> RabbitMQ Shared Connection Established");
    }
  }

  public static void publish(int id, String type, String payload) throws Exception {
    try {
      init();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> msg = new HashMap<>();
      msg.put("id", id);
      msg.put("type", type);
      msg.put("payload", mapper.readTree(payload));

      String message = mapper.writeValueAsString(msg);
      channel.basicPublish("", QUEUE, null, message.getBytes());
      LOGGER.info("Published task to queue: ", id);

    } catch (Exception e) {
      LOGGER.error("Failed to publish task {}: ", id, e);
    }
  }
}
