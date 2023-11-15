package com.knot.uol.RabbitMQ;

import java.io.*;
import java.util.concurrent.TimeoutException;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import com.rabbitmq.client.*;


public class RabbitMQHandler extends AbstractMediator { 

	public boolean mediate(MessageContext mc) { 
		
		  
		  //org.apache.axis2.context.MessageContext axis2MessageContext = ((org.apache.axis2.context.MessageContext) mc);
		  //String messages=  axis2MessageCo	ntext.getEnvelope().getBody().getFirstElement().toString();
		  ConnectionFactory cf = new ConnectionFactory(); 
		  String messages=(String) mc.getProperty("messages");
		  System.out.println(messages);
		  cf.setHost("localhost");
		  cf.setUsername("guest");
		  cf.setPassword("guest");
		  cf.setPort(5672);
		  try {
			  String queueName="priorTestQueue";
			Connection connection = cf.newConnection();
			Channel channel = connection.createChannel();
			AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
			int messageCount = declareOk.getMessageCount();
			System.out.println(messageCount);
			String priority=(String) mc.getProperty("priority");
			int prior = Integer.parseInt(priority);
	
			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
	                .priority(prior)//setting priority
	                .deliveryMode(2)// message persistance
	                .build();
			 channel.basicPublish("priorTest","priorTestQueue", props, messages.getBytes("UTF-8"));// args=EXCHANGE,queue,properties,message.
			 System.out.println("pushed");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			log.error(e.getCause());
			System.out.println("error");
		} catch (TimeoutException e) {
		
			
			e.printStackTrace();
			System.out.println(e.getMessage());
			log.error(e.getCause());
			System.out.println("error");
		}
		return true;
	}
	public static void main(String[] args) {
		System.out.println("jyo");
	}
	
}
