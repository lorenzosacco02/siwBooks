package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Author;
import it.uniroma3.siw.siwbooks.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id).get();
    }

    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    public void save(Author movie) {
        authorRepository.save(movie);
    }

    public List<Author> searchByName(String name) {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(name);
        authors.addAll(authorRepository.findBySurnameContainingIgnoreCase(name));
        return authors;
    }
}
