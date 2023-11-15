package com.uol.knot;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumeRabbit {
  private final static String QUEUE_NAME = "PriorQueue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
       ExecutorService executor = Executors.newFixedThreadPool(10);
       channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                int priority = properties.getPriority();
                try
                {
					Thread.sleep(5000);
				} 
                catch (InterruptedException e) {
					
					e.printStackTrace();
				}

                executor.execute(() -> {
                    System.out.println("Processing message: " + message + " with priority: " + priority);
                });
            }
        });
    }
}
