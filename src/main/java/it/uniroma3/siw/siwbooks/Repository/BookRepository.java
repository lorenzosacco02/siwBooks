package it.uniroma3.siw.siwbooks.Repository;

import it.uniroma3.siw.siwbooks.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
