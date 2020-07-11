package me.kostasakrivos.springboot.demo.springbootdemo.service;

import me.kostasakrivos.springboot.demo.springbootdemo.dao.PersonDao;
import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonDao personDao;

    @Autowired
    public PersonService(@Qualifier("postgres") PersonDao personDao) {
        this.personDao = personDao;
    }

    public Integer addPerson(Person person) {
        return personDao.insertPerson(person);
    }

    public List<Person> fetchPersons() {
        return personDao.fetchPersons();
    }

    public Optional<Person> fetchPersonById(UUID id) {
        return personDao.fetchPersonById(id);
    }

    public Integer removePersonById(UUID id) {
        return personDao.removePersonById(id);
    }

    public Integer updatePerson(UUID id, Person newPerson) {
        return personDao.updatePersonById(id, newPerson);
    }
}
