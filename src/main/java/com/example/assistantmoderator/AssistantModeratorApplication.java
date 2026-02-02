package com.example.assistantmoderator;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class AssistantModeratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssistantModeratorApplication.class, args);
    }
}
