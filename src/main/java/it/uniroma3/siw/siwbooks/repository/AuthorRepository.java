package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Author;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);

    Collection<? extends Author> findBySurnameContainingIgnoreCase(String name);

    List<Author> findByNameStartingWithIgnoreCase(String prefix);

    List<Author> findBySurnameStartingWithIgnoreCase(String prefix);

    @Modifying
    @Query(value = "DELETE FROM book_authors WHERE authors_id = :authors_id AND written_id = :written_id", nativeQuery = true)
    void deleteBookFromAuthor(@Param("authors_id") Long authors_id, @Param("written_id") Long written_id);

    @Modifying
    @Query(value = "DELETE FROM book_authors WHERE authors_id = :authors_id", nativeQuery = true)
    void deleteAllBookFromAuthor(@Param("authors_id") Long authors_id);

}
