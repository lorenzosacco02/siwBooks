package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).get();
    }

    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    public void save(Book movie) {
        bookRepository.save(movie);
    }

}
