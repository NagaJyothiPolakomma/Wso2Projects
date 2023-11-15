package com.uol.knot;

import java.io.*;

import org.apache.synapse.*;
import org.apache.synapse.mediators.AbstractMediator;

import com.rabbitmq.client.*;


public class ProduceRabbit extends AbstractMediator 
{ 
private static final String RABBITMQ_HOST = "localhost";
  private static final int RABBITMQ_PORT = 5672;
  private static final String RABBITMQ_USERNAME = "guest";
  private static final String RABBITMQ_PASSWORD = "guest";
  private static final String RABBITMQ_QUEUE_NAME = "PriorQueue";

  @Override
  public boolean mediate(MessageContext mc) 
  {
      try 
      {
    	  ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(RABBITMQ_PORT);
        factory.setUsername(RABBITMQ_USERNAME);
        factory.setPassword(RABBITMQ_PASSWORD);
        String priorityString = (String) mc.getProperty("priority");
        System.out.println(priorityString);
        String message = (String) mc.getProperty("message");
        System.out.println(message);

        int priority;
        try {
            priority = Integer.parseInt(priorityString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid priority value: " + priorityString);
            return true;
        }


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
          AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                  .priority(priority)
                  .build();

          // Publish the message with the specified priority
          channel.basicPublish("PriorExchange", RABBITMQ_QUEUE_NAME, properties, message.getBytes());

          System.out.println("Message sent successfully to RabbitMQ");
          
      }
      catch(Exception e)
      {
    	  System.out.println("Error:"+e.getMessage());
      }
	return true;
  }
public static void main(String[] args)
{
		
  	System.out.println("jyo");
	}
}
