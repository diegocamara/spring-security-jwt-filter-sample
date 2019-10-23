package com.example.basicjwtfilter;

import com.example.basicjwtfilter.properties.JwtProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BasicJwtFilterApplication {

  public static void main(String[] args) {
    SpringApplication.run(BasicJwtFilterApplication.class, args);
  }
}
