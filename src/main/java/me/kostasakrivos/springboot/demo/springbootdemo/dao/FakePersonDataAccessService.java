package me.kostasakrivos.springboot.demo.springbootdemo.dao;

import me.kostasakrivos.springboot.demo.springbootdemo.EmailValidator;
import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import me.kostasakrivos.springboot.demo.springbootdemo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.kostasakrivos.springboot.demo.springbootdemo.dao.UpdatePersonValidator.updateGivenInfo;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private final EmailValidator emailValidator;
    private final static List<Person> DB = new ArrayList<>();

    static { DB.add(new Person(UUID.randomUUID(), "Kostas", "Akrivos", "test@gmail.com")); }

    @Autowired
    public FakePersonDataAccessService(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }

    @Override
    public Integer insertPerson(UUID id, Person person) {
        final boolean isEmailValid = emailValidator.test(person.email());

        if (!isEmailValid) {
            throw new ApiRequestException(person.email() + " is an invalid email");
        } else {
            DB.add(
                new Person(id, person.firstName(), person.lastName(), person.email())
            );
            return 1;
        }
    }

    @Override
    public List<Person> fetchPersons() {
        return DB;
    }

    @Override
    public Integer removePersonById(UUID id) {
        final Optional<Person> personMaybe = fetchPersonById(id);
        if (personMaybe.isPresent()) {
            DB.remove(personMaybe.get());
            return 1;
        }
        else return 0;
    }

    @Override
    public Optional<Person> fetchPersonById(UUID id) {
        return DB.stream()
                .filter(p -> p.id().equals(id))
                .findFirst();
    }

    @Override
    public Integer updatePersonById(UUID id, Person person) {
        final boolean isEmailValid = emailValidator.test(person.email());

        if (!isEmailValid) {
            throw new ApiRequestException(person.email() + " is an invalid email");
        } else {
            return fetchPersonById(id)
                    .map(found -> {
                        if (DB.contains(found)) {
                            Person personToUpdate = new Person(id, person.firstName(), person.lastName(), person.email());
                            DB.set(
                                    DB.indexOf(found),
                                    updateGivenInfo(found, personToUpdate)
                            );
                            return 1;
                        } else {
                            return 0;
                        }
                    }).orElse(0);
        }
    }
}
