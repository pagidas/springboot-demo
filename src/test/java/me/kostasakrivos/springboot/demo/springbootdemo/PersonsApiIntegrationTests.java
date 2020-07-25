package me.kostasakrivos.springboot.demo.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:application.yml")
class PersonsApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private String api(String uri) {
        return "/api/v1" + uri;
    }

    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(api("/healthcheck")))
                .andExpect(
                    status().isOk()
                );
    }

}
