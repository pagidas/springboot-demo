package me.kostasakrivos.springboot.demo.springbootdemo.dao;

import me.kostasakrivos.springboot.demo.springbootdemo.EmailValidator;
import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import me.kostasakrivos.springboot.demo.springbootdemo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.kostasakrivos.springboot.demo.springbootdemo.dao.UpdatePersonValidator.updateGivenInfo;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private final JdbcTemplate jdbc;
    private final EmailValidator emailValidator;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbc, EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
        this.jdbc = jdbc;
    }

    @Override
    public Integer insertPerson(UUID id, Person person) {
        final boolean emailIsValid = emailValidator.test(person.email());

        if (!emailIsValid) {
            throw new ApiRequestException(person.email() + " is an invalid email");
        } else {
            return jdbc.update(
                    "INSERT INTO person (id, first_name, last_name, email) VALUES (?, ?, ?, ?)",
                    id,
                    person.firstName(),
                    person.lastName(),
                    person.email()
            );
        }
    }

    @Override
    public List<Person> fetchPersons() {
        return jdbc.query(
                "SELECT id, first_name, last_name, email FROM person",
                (resultSet, i) -> new Person(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                )
        );
    }

    @Override
    public Integer removePersonById(UUID id) {
        return jdbc.update(
            "DELETE FROM person WHERE id = ?",
                id
        );
    }

    @Override
    public Optional<Person> fetchPersonById(UUID id) {
        return Optional.ofNullable(
            jdbc.queryForObject(
                "SELECT id, first_name, last_name, email FROM person WHERE id = ?",
                new Object[] { id },
                (resultSet, i) -> new Person(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                )
            )
        );
    }

    @Override
    public Integer updatePersonById(UUID id, Person person) {
        final boolean isEmailValid = emailValidator.test(person.email());

        if (!isEmailValid) {
            throw new ApiRequestException(person.email() + " is an invalid email");
        } else {
            return fetchPersonById(id).map(found -> {
                final Person personToUpdate = updateGivenInfo(
                        found,
                        new Person(id, person.firstName(), person.lastName(), person.email())
                );

                return jdbc.update(
                        "UPDATE person SET first_name = ?, last_name = ?, email = ? WHERE id = ?",
                        personToUpdate.firstName(),
                        personToUpdate.lastName(),
                        personToUpdate.email(),
                        personToUpdate.id()
                );
            }).orElse(0);
        }
    }
}
