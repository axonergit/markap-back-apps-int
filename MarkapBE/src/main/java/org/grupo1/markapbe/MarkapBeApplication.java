package org.grupo1.markapbe;

import org.grupo1.markapbe.persistence.entity.*;
import org.grupo1.markapbe.persistence.repository.CategoryRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class MarkapBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkapBeApplication.class, args);
    }

}
