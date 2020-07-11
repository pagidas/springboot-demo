package me.kostasakrivos.springboot.demo.springbootdemo.dao;

import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {

    Integer insertPerson(UUID id, Person person);

    List<Person> fetchPersons();

    Integer removePersonById(UUID id);

    Optional<Person> fetchPersonById(UUID id);

    Integer updatePersonById(UUID id, Person person);

    default Integer insertPerson(Person person) {
        return insertPerson(UUID.randomUUID(), person);
    }
}
