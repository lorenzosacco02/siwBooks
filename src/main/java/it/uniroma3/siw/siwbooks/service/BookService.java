package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Image;
import it.uniroma3.siw.siwbooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private ImageService imageService;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void save(Book movie) {
        bookRepository.save(movie);
    }

    @Transactional
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    public void registerBook(Book book, MultipartFile photo, MultipartFile[] images) throws IOException {
        if (!photo.isEmpty()) {
            Image image = new Image();
            image.setName(photo.getOriginalFilename());
            image.setContent(photo.getBytes());
            imageService.save(image);

            book.setCover(image);
        }

        for (MultipartFile img : images) {
            if (!img.isEmpty()) {
                Image image = new Image();
                image.setName(img.getOriginalFilename());
                image.setContent(img.getBytes());
                imageService.save(image);

                book.getImages().add(image);
            }
        }

        this.save(book);
    }
}
