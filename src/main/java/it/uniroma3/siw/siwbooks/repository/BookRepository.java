package it.uniroma3.siw.siwbooks.repository;

import it.uniroma3.siw.siwbooks.model.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Modifying
    @Query(value = "DELETE FROM book_authors WHERE authors_id = :authors_id AND written_id = :written_id", nativeQuery = true)
    void deleteBookFromAuthor(@Param("written_id") Long written_id, @Param("authors_id") Long authors_id);

    @Modifying
    @Query(value = "DELETE FROM book_authors WHERE written_id = :written_id", nativeQuery = true)
    void deleteAllAuthorFromBook(@Param("written_id") Long written_id);

}



