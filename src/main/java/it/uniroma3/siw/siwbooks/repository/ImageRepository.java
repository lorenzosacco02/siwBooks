package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Image;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


public interface ImageRepository extends CrudRepository<Image, Long> {
}
