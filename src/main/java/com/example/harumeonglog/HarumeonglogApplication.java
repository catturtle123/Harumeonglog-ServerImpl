package com.example.harumeonglog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HarumeonglogApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarumeonglogApplication.class, args);
    }

}
