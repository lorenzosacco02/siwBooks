package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByBook(Book book);
}
