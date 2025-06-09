package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
