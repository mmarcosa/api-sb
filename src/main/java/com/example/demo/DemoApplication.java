package com.example.demo;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws NamingException, JMSException {
		SpringApplication.run(DemoApplication.class, args);
	}

}
