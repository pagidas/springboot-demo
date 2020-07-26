package me.kostasakrivos.springboot.demo.springbootdemo.smoke;

import me.kostasakrivos.springboot.demo.springbootdemo.SpringbootDemoApplication;
import me.kostasakrivos.springboot.demo.springbootdemo.api.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SpringbootDemoApplication.class)
@TestPropertySource(value = "classpath:application.yml")
class SmokeTests {

	@Autowired
	private PersonController personController;

	@Test
	public void contextLoads() {
		assertThat(personController).isNotNull();
	}
}
