package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);

    Collection<? extends Author> findBySurnameContainingIgnoreCase(String name);

    List<Author> findByNameStartingWithIgnoreCase(String prefix);

    List<Author> findBySurnameStartingWithIgnoreCase(String prefix);

}
