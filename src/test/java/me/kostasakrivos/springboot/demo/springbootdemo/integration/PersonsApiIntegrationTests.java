package me.kostasakrivos.springboot.demo.springbootdemo.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.kostasakrivos.springboot.demo.springbootdemo.SpringbootDemoApplication;
import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringbootDemoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.yml")
class PersonsApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String api(String uri) {
        return "/api/v1" + uri;
    }

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update(
                "INSERT INTO PERSON (id, first_name, last_name, email) VALUES (?, ?, ?, ?)",
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@gmail.com"
        );

        jdbcTemplate.update(
                "INSERT INTO PERSON (id, first_name, last_name, email) VALUES (?, ?, ?,?)",
                UUID.randomUUID(),
                "Mr.",
                "Robot",
                "mr.robot@gmail.com"
        );
    }

    @AfterEach
    public void cleanUp() {
        jdbcTemplate.update("DELETE FROM PERSON");
    }

    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(get(api("/healthcheck")))
                .andExpect(
                    status().isOk()
                );
    }

    @Test
    public void canRequestAllPersons() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});

        assertThat(persons).hasSize(2);
    }

    @Test
    public void canRequestPersonById() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});

        mockMvc.perform(get(api("/persons/" + persons.get(0).id())))
                .andExpect(
                    status().isOk()
                );
    }

    @Test
    public void canRequestDeletePersonById() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println(bodyString);

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});

        mockMvc.perform(delete(api("/persons/" + persons.get(0).id())))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    public void canRequestNewPerson() throws Exception {
        String newPersonInJson = mapper.writeValueAsString(
            new Person(UUID.randomUUID(), "Kostas", "Akrivos", "test@gmail.com")
        );

        mockMvc.perform(post(api("/persons"))
                .contentType(MediaType.APPLICATION_JSON).content(newPersonInJson)
        ).andExpect(status().isCreated());
    }

    @Test
    public void canRequestUpdatePersonById() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});
        UUID givenId = persons.get(0).id();

        String newPersonInJson = mapper.writeValueAsString(
                new Person(givenId, "Kostas", "Akrivos", "test@gmail.com")
        );

        mockMvc.perform(put(api("/persons/" + givenId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPersonInJson)
        ).andExpect(
            status().isOk()
        );
    }
}
