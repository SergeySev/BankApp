package com.example.telproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelProjectApplication.class, args);
    }


}
