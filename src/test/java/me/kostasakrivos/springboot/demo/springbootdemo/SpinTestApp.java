package me.kostasakrivos.springboot.demo.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootApplication
@TestConfiguration("classpath:application.yml")
public class SpinTestApp {
    public static void main(String[] args) { SpringApplication.run(SpinTestApp.class, args); }
}
