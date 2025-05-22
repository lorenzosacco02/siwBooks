package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
