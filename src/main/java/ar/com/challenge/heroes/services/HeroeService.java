package ar.com.challenge.heroes.services;

import ar.com.challenge.heroes.entities.Heroe;
import ar.com.challenge.heroes.ex.HeroeNotFoundException;
import ar.com.challenge.heroes.repository.HeroeRepository;
import ar.com.challenge.heroes.reqres.HeroeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "product")
@Service
public class HeroeService {

  @Autowired
  private HeroeRepository heroeRepository;

  @Caching(evict = {@CacheEvict(value = "getAllHeroescache", allEntries = true),
          @CacheEvict(value = "heroecache", key = "#heroeRequest.nombre")
  })
  public Long createNewHeroe(HeroeRequest heroeRequest) {
    Heroe heroe = new Heroe();
    heroe.setNombre(heroeRequest.getNombre());
    heroe = heroeRepository.save(heroe);
    return heroe.getId();
  }

  @Cacheable(value = "getAllHeroescache")
  public List<Heroe> getAllHeroes() {
    return heroeRepository.findAll();
  }

  public List<Heroe> findByNombreContainingIgnoreCase(String nombre) {
    return heroeRepository.findByNombreContainingIgnoreCase(nombre);
  }


  public Heroe getHeroesById(Long id) {
    Optional<Heroe> requestedHeroes = heroeRepository.findById(id);

    if (requestedHeroes.isEmpty()) {
      throw new HeroeNotFoundException(String.format("Heroes with id: '%s' not found", id));
    }

    return requestedHeroes.get();
  }

  @Caching(evict = {@CacheEvict(value = "getAllHeroescache", allEntries = true),
          @CacheEvict(value = "heroecache", key = "#heroeToUpdateRequest.nombre")
  })
  @Transactional
  public Heroe updateHeroes(Long id, HeroeRequest heroeToUpdateRequest) {

    Optional<Heroe> heroeFromDatabase = heroeRepository.findById(id);

    if (heroeFromDatabase.isEmpty()) {
      throw new HeroeNotFoundException(String.format("Heroe with id: '%s' not found", id));
    }
    Heroe heroeToUpdate = heroeFromDatabase.get();
    heroeToUpdate.setNombre(heroeToUpdateRequest.getNombre());
    return heroeToUpdate;
  }

  public void deleteHeroeById(Long id) {
    heroeRepository.deleteById(id);
  }
}
