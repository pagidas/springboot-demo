package me.kostasakrivos.springboot.demo.springbootdemo.dao;

import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;

class UpdatePersonValidator {
    public static Person updateGivenInfo(Person found, Person personToUpdate) {
        final String updateFirstName;
        final String updateLastName;
        final String updateEmail;

        if (isBlankOrEmptyOrNull(personToUpdate.firstName()))
            updateFirstName = found.firstName();
        else
            updateFirstName = personToUpdate.firstName();

        if (isBlankOrEmptyOrNull(personToUpdate.lastName()))
            updateLastName = found.lastName();
        else
            updateLastName = personToUpdate.lastName();

        if (isBlankOrEmptyOrNull(personToUpdate.email()))
            updateEmail = found.email();
        else
            updateEmail = personToUpdate.email();

        return new Person(personToUpdate.id(), updateFirstName, updateLastName, updateEmail);
    }

    private static boolean isBlankOrEmptyOrNull(String strLiteral) {
        return strLiteral == null || strLiteral.isEmpty() || strLiteral.isBlank();
    }
}

