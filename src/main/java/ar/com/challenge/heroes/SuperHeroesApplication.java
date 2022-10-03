package ar.com.challenge.heroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication
@ComponentScan("ar.com.challenge.heroes")
public class SuperHeroesApplication {

  public static void main(String[] args) {
    SpringApplication.run(SuperHeroesApplication.class, args);
  }

}
