package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Author;
import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Image;
import it.uniroma3.siw.siwbooks.repository.AuthorRepository;
import it.uniroma3.siw.siwbooks.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorService {

    @Autowired
    private ImageRepository imageService;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookService bookService;

    @Transactional
    public Author findById(Long id) {
        return authorRepository.findById(id).get();
    }

    @Transactional
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Transactional
    public List<Author> searchByNameOrSurname(String name) {
        List<Author> authors = authorRepository.findByNameStartingWithIgnoreCase(name);
        authors.addAll(authorRepository.findBySurnameStartingWithIgnoreCase(name));
        Set<Author> set = new LinkedHashSet<>(authors); // mantiene l'ordine
        authors = new ArrayList<>(set);
        return authors;
    }

    @Transactional
    public void update(Long id, String name, String surname, LocalDate dateOfBirth, LocalDate dateOfDeath) {
        Author author = this.findById(id);
        author.setName(name);
        author.setSurname(surname);
        author.setDateOfBirth(dateOfBirth);
        author.setDateOfDeath(dateOfDeath);
        authorRepository.save(author);
    }

    @Transactional
    public void registerAuthor(Author author, MultipartFile photo) throws IOException {

        if (!photo.isEmpty()) {
            Image image = new Image();
            image.setName(photo.getOriginalFilename());
            image.setContent(photo.getBytes());
            imageService.save(image);

            author.setImage(image);
        }

        List<Author> newAuthor = new ArrayList<Author>();
        newAuthor.add(author);
        for (Book book : author.getWritten()) {
            book.setAuthors(newAuthor);
        }

        this.save(author);
    }
}
