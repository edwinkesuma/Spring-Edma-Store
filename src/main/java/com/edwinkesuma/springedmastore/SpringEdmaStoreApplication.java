package com.edwinkesuma.springedmastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringEdmaStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEdmaStoreApplication.class, args);
    }

}
