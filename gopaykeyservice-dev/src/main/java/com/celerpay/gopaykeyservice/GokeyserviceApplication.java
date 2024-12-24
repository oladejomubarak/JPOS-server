package com.celerpay.gopaykeyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GokeyserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GokeyserviceApplication.class, args);
    }

}
