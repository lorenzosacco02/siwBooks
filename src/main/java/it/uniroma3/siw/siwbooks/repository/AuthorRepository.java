package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
