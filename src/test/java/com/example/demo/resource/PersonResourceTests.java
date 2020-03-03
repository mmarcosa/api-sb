package com.example.demo.resource;

import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@RunWith( SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonResourceTests {

    @Autowired
    private TestRestTemplate testTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config{
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder().basicAuthentication("", "");
        }
    }

   @Test
   public void listPersons(){
       Person person = new Person(1, "Marcos", 25);
       BDDMockito.when(personRepository.findAll()).thenReturn(Collections.singletonList(person));
       testTemplate = testTemplate.withBasicAuth("1", "1");
       ResponseEntity<String> response = testTemplate.getForEntity("/persons", String.class);
       Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
   }

    @Test
    public void getHello(){
        testTemplate = testTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = testTemplate.getForEntity("/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isEqualTo("Opa, to no ar!");
    }

}
