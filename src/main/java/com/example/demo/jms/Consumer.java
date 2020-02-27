package com.example.demo.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.*;
import javax.naming.InitialContext;

import com.example.demo.repositories.PersonRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.Person;
import com.google.gson.Gson;

@Component
public class Consumer {

	private List<Person> persons = new ArrayList<>();
	private PersonRepository personRepository;

	public Consumer(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Scheduled(fixedDelay = 10000)
	public void connectToRow() throws Exception {
	    InitialContext context = new InitialContext();
	    //imports do package javax.jms
	    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
	    Connection connection = factory.createConnection();
	    connection.start();
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	    Destination fila = (Destination) context.lookup("pessoa");

	    this.persons = readFromMQ(fila, session);
		this.save(persons);

	    session.close();
	    connection.close();
	    context.close();
	}

	private List<Person> readFromMQ(Destination fila, Session session) throws JMSException {
		List<Person> persons = new ArrayList<>();
		MessageConsumer consumer = session.createConsumer(fila);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println(textMessage.getText());
					Person person = new Person();
					Gson gson = new Gson();
					person = gson.fromJson(textMessage.getText(), Person.class);
					persons.add(person);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		return persons;
	}

	private void save (List<Person> persons) {
		personRepository.saveAll(persons);
	}

}
