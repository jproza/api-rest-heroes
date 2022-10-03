package ar.com.challenge.heroes.repository;

import ar.com.challenge.heroes.entities.Heroe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroeRepository extends JpaRepository<Heroe, Long> {

    List<Heroe> findByNombreContainingIgnoreCase(String nombre);

}
