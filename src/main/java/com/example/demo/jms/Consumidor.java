package com.example.demo.jms;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.Person;
import com.google.gson.Gson;

@Component
public class Consumidor {

	@Scheduled(fixedDelay = 10000)
	public void connectToRow() throws Exception {
	
	    InitialContext context = new InitialContext(); 
	
	    //imports do package javax.jms
	    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
	    Connection connection = factory.createConnection();
	    connection.start();
	
	    List<Person> persons = new ArrayList<>();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("pessoa");
				
		MessageConsumer consumer = session.createConsumer(fila);
	    
		Message message = consumer.receive();
		
		System.out.println("Recebendo mensagem: " + message);
		
		Gson gson = new Gson();
		
		Person person = new Person();
		//person = gson.fromJson(message.getObjectProperty("text"), Person.class);
		
	
	    session.close();
	    connection.close();
	    context.close();
	}
	
}
