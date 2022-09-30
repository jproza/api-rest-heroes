package ar.com.challenge.heroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SuperHeroesApplication {

  public static void main(String[] args) {
    SpringApplication.run(SuperHeroesApplication.class, args);
  }

}
