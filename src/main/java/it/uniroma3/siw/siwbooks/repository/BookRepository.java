package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
}
