package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.model.Author;
import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Image;
import it.uniroma3.siw.siwbooks.service.AuthorService;
import it.uniroma3.siw.siwbooks.service.BookService;
import it.uniroma3.siw.siwbooks.service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;

    @GetMapping("/author/{author_id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long author_id) {
        Author author = authorService.findById(author_id);
        Image image = author.getImage();

        if (image == null || image.getContent() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // o IMAGE_PNG se il formato lo richiede
        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/author/{author_id}")
    public String getAuthor(@PathVariable Long author_id, Model model) {
        Author author = authorService.findById(author_id);
        model.addAttribute("author", author);
        return "author";
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

    @GetMapping("/admin/formNewAuthor/addBook/{id}")
    public String getAllBooks(Model model,
                              HttpSession session,
                              @PathVariable Long id) {
        Author author = (Author) session.getAttribute("author");
        Book book = bookService.findById(id);
        List<Book> writtenBooks = author.getWritten();
        if (!writtenBooks.contains(book)) {
            author.getWritten().add(book);
        }
        model.addAttribute("author", author);
        List<Book> newBooks = (List<Book>)bookService.findAll();
        newBooks.removeAll(writtenBooks);
        model.addAttribute("books", newBooks);
        return "admin/formNewAuthor";
    }

    @GetMapping("/admin/addAuthor")
    public String addAuthor(Model model,
                            HttpSession session) {
        Author author = (Author) session.getAttribute("author");
        if (author == null){
            author = new Author();
        }
        session.setAttribute("author", author);
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("author", author);
        return "admin/formNewAuthor";
    }


    @PostMapping("/admin/addAuthor")
    public String saveAuthor(@ModelAttribute("author") Author author,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("photo") MultipartFile photo,
                             HttpSession session,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            session.setAttribute("author", author);
            model.addAttribute("author", author);
            return "admin/formNewAuthor";
        }

        try {
        authorService.registerAuthor(author, photo);
        }
        catch (IOException e) {
            return "redirect:/";
        }
        session.removeAttribute("author");
        redirectAttributes.addFlashAttribute("success", "Autore aggiunto con successo!");
        return "redirect:/author/" + author.getId();
    }
}
