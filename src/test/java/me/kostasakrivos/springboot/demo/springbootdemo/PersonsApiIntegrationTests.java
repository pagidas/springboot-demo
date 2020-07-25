package me.kostasakrivos.springboot.demo.springbootdemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.kostasakrivos.springboot.demo.springbootdemo.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.yml")
@Sql("classpath:db.test/dummy_data_persons.sql")
class PersonsApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private String api(String uri) {
        return "/api/v1" + uri;
    }

    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(get(api("/healthcheck")))
                .andExpect(
                    status().isOk()
                );
    }

    @Test
    public void getPersons() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});

        assertThat(persons).hasSize(2);
    }

    @Test
    public void getPersonById() throws Exception {
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
    public void deletePersonById() throws Exception {
        String bodyString = mockMvc.perform(get(api("/persons")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Person> persons = mapper.readValue(bodyString, new TypeReference<>() {});

        mockMvc.perform(delete(api("/persons/" + persons.get(0).id())))
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    public void newPerson() throws Exception {
        String newPersonInJson = mapper.writeValueAsString(
            new Person(UUID.randomUUID(), "Kostas", "Akrivos", "test@gmail.com")
        );

        mockMvc.perform(post(api("/persons"))
                .contentType(MediaType.APPLICATION_JSON).content(newPersonInJson)
        ).andExpect(status().isCreated());
    }

    @Test
    public void updatePerson() throws Exception {
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
