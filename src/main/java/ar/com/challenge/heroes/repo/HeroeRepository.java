package ar.com.challenge.heroes.repo;

import ar.com.challenge.heroes.entities.Heroe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroeRepository extends JpaRepository<Heroe, Long> {

    List<Heroe> findByNombreContainingIgnoreCase(String nombre);

}
