package com.ferry.sunservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class SunServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunServiceApplication.class, args);
    }

}
