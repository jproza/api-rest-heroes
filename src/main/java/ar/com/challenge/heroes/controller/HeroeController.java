package ar.com.challenge.heroes.controller;

import ar.com.challenge.heroes.entities.Heroe;
import ar.com.challenge.heroes.reqres.HeroeRequest;
import ar.com.challenge.heroes.services.HeroeService;
import ar.com.challenge.heroes.logging.LogEntryExit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/heroes")
public class HeroeController {

  @Autowired
  private HeroeService heroeService;

  @LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @PostMapping
  public ResponseEntity<Void> createNewHeroe(@Valid @RequestBody HeroeRequest heroeRequest, UriComponentsBuilder uriComponentsBuilder) {
    Long primaryKey = heroeService.createNewHeroe(heroeRequest);

    UriComponents uriComponents = uriComponentsBuilder.path("/api/heroes/{id}").buildAndExpand(primaryKey);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uriComponents.toUri());

    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
  }
  @LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @GetMapping
  public ResponseEntity<List<Heroe>> getAllHeroes() {
    return ResponseEntity.ok(heroeService.getAllHeroes());
  }
  @LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @GetMapping("/{id}")
  public ResponseEntity<Heroe> getHeroeById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(heroeService.getHeroesById(id));
  }
  @LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @PutMapping("/{id}")
  public ResponseEntity<Heroe> updateHeroe(@PathVariable("id") Long id, @Valid @RequestBody HeroeRequest heroeRequest) {
    return ResponseEntity.ok(heroeService.updateHeroes(id, heroeRequest));
  }
  @LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHeroe(@PathVariable("id") Long id) {
    heroeService.deleteHeroeById(id);
    return ResponseEntity.ok().build();
  }

}
