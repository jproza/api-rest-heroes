package ar.com.challenge.heroes.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Heroe {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String nombre;


}
