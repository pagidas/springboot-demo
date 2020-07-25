package me.kostasakrivos.springboot.demo.springbootdemo.api;

import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import me.kostasakrivos.springboot.demo.springbootdemo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v1")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value = "/healthcheck", produces = "application/json;charset=utf-8")
    public String getHealthCheck() {
        return "{ \"isWorking\": true }";
    }

    @GetMapping("/persons")
    public List<Person> getPersons() {
        return personService.fetchPersons();
    }

    @PostMapping("/persons")
    @ResponseStatus(CREATED)
    public void addPerson(@RequestBody Person person) {
        personService.addPerson(person);
    }

    @GetMapping("/persons/{id}")
    public Person getPersonById(@PathVariable("id") UUID id) {
        return personService.fetchPersonById(id)
                .orElse(null);
    }

    @DeleteMapping("/persons/{id}")
    public void deletePersonById(@PathVariable("id") UUID id) {
        personService.removePersonById(id);
    }

    @PutMapping("/persons/{id}")
    public void updatePerson(@PathVariable("id") UUID id, @RequestBody Person personToUpdate) {
        personService.updatePerson(id, personToUpdate);
    }
}
