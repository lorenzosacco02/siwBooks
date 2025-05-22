package it.uniroma3.siw.siwbooks.Repository;

import it.uniroma3.siw.siwbooks.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
