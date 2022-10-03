package ar.com.challenge.heroes;

import ar.com.challenge.heroes.entities.Heroe;
import ar.com.challenge.heroes.repository.HeroeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeroeInitializer implements CommandLineRunner {

  @Autowired
  private HeroeRepository heroeRepository;

  @Override
  public void run(String... args) throws Exception {

    log.info("Starting heroes initialization ...");

    Heroe heroe = new Heroe();
    heroe.setNombre("X-Men");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Hulk");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Capitán América");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Thor");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Spider-Man");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Superman");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Spiderman");
    heroeRepository.save(heroe);

    heroe = new Heroe();
    heroe.setNombre("Flash");
    heroeRepository.save(heroe);



    log.info("... finished heroes initialization");

  }
}
