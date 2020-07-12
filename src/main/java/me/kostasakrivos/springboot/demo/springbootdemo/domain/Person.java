package me.kostasakrivos.springboot.demo.springbootdemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Person {

    @JsonProperty
    private final UUID id;

    @JsonProperty
    private final String firstName;

    @JsonProperty
    private final String lastName;

    @JsonProperty
    private final String email;
}
