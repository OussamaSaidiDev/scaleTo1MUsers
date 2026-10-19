package com.exemple.oussama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The H2 database is created and populated at startup from
 * src/main/resources/schema.sql and data.sql.
 **/
@SpringBootApplication
public class UserApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApiApplication.class, args);
  }
}
