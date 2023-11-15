package com.knot.uol.RabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class consumer {
	public static void main(String[] args) throws IOException, TimeoutException {
		 ConnectionFactory cf = new ConnectionFactory();
		
		  cf.setHost("localhost");
		  cf.setUsername("guest");
		  cf.setPassword("guest");
		  cf.setPort(5672);

	       Connection connection = cf.newConnection();
	             Channel channel = connection.createChannel();
	             AtomicInteger activeThreadCount = new AtomicInteger(0);    
		channel.basicConsume("priorTestQueue", new DefaultConsumer(channel) {
			 @Override
               public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                   String message = new String(body, "UTF-8");
                   System.out.println("Received message with priority " + properties.getPriority() + ": " + message);
                   
                   channel.basicQos(2);
                   activeThreadCount.incrementAndGet();
                   // Simulate message processing
//                   try {
//                      // Thread.sleep(10000);
//                   } catch (InterruptedException e) {
//                       Thread.currentThread().interrupt();
//                   }

                   // Acknowledge the message to remove it from the queue
                   channel.basicAck(envelope.getDeliveryTag(), false);
               }
		 });
	}

}
