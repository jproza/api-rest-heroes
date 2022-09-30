package ar.com.challenge.heroes.loginflow.repository;

import ar.com.challenge.heroes.loginflow.models.ERole;
import ar.com.challenge.heroes.loginflow.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
