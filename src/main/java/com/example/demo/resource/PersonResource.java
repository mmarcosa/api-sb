package com.example.demo.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path="/persons")
public class PersonResource {

	private PersonRepository personRepository;

	public PersonResource(PersonRepository personRepository) {
		super();
		this.personRepository = personRepository;
	}
	
	@ApiOperation("Cadastra pessoas, uma por vez.")
	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person){
		personRepository.save(person);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@ApiOperation("Consulta pessoas, retornando todas em uma lista.")
	@GetMapping
	public ResponseEntity<List<Person>> getAll(){
		List<Person> persons = new ArrayList<>();
		persons = personRepository.findAll();
		return new ResponseEntity<>(persons, HttpStatus.OK);
	}
	
	@ApiOperation("Consulta pessoa pelo id.")
	@GetMapping(path="/{id}")
	public ResponseEntity<Optional<Person>> getById(@PathVariable Integer id){
		Optional<Person> person;
		try {
			person = personRepository.findById(id);
			return new ResponseEntity<Optional<Person>>(person, HttpStatus.OK);
		}catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation("Deleta pessoas, uma por vez.")
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Optional<Person>> deleteById(@PathVariable Integer id){
		try {
			personRepository.deleteById(id);
			return new ResponseEntity<Optional<Person>>(HttpStatus.OK);
		}catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation("Atualiza dados de uma pessoa.")
	@PutMapping(value="/{id}")
	public ResponseEntity<Person> update(@PathVariable Integer id, @RequestBody Person newPerson){		
		 return personRepository.findById(id)
		           .map(person -> {
		        	   person.setName(newPerson.getName());
		        	   person.setAge(newPerson.getAge());
		        	   Person personoUpdated = personRepository.save(person);
		               return ResponseEntity.ok().body(personoUpdated);
		           }).orElse(ResponseEntity.notFound().build());
	}

}
