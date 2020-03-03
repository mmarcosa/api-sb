package com.example.demo.repository;

import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void save(){
        Person person = new Person("Marcos", 28);
        this.personRepository.save(person);
        assertThat(person.getId()).isNotNull();
        assertThat(person.getAge()).isEqualTo(28);
        assertThat(person.getName()).isEqualTo("Marcos");
    }

    @Test
    public void update() {
        Person person = new Person("Marcos", 28);
        this.personRepository.save(person);
        person.setAge(65);
        person.setName("José");
        this.personRepository.save(person);
        assertThat(personRepository.getByName("José")).isEqualTo(person);
    }

    @Test
    public void delete() {
        Person person = new Person("Marcos", 28);
        this.personRepository.save(person);
        personRepository.delete(person);
        assertThat(personRepository.findById(person.getId())).isEmpty();
    }

}
