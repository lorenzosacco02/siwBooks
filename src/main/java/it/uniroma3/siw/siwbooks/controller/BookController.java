package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.controller.validator.BookValidator;
import it.uniroma3.siw.siwbooks.model.*;
import it.uniroma3.siw.siwbooks.service.AuthorService;
import it.uniroma3.siw.siwbooks.service.BookService;
import it.uniroma3.siw.siwbooks.service.ImageService;
import it.uniroma3.siw.siwbooks.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @Autowired
    ImageService imageService;

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookValidator bookValidator;
    @Autowired
    private UserService userService;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Dici a Spring: non provare a bindare automaticamente il campo `images`
        binder.setDisallowedFields("images");
    }

    @GetMapping("/book/{book_id}/cover")
    public ResponseEntity<byte[]> getImage(@PathVariable Long book_id) {
        Book book = bookService.findById(book_id);
        Image image = book.getCover();

        if (image == null || image.getContent() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // o IMAGE_PNG se il formato lo richiede
        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/book/{book_id}/images/{img_id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long book_id,
                                           @PathVariable Long img_id) {
        Book book = bookService.findById(book_id);
        Image image = imageService.getImage(img_id);

        if (image == null || image.getContent() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // o IMAGE_PNG se il formato lo richiede
        return new ResponseEntity<>(image.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/book/{book_id}")
    public String getBook(@PathVariable Long book_id, Model model) {
        Book book = bookService.findById(book_id);
        model.addAttribute("book", book);
        boolean isRecensito = false;
        User user =  userService.getCurrentUser();
        if(user != null) {
            for(Review review : user.getReviews()) {
                if(review.getBook().getId().equals(book_id)){
                    isRecensito = true;
                }
            }
        }
        model.addAttribute("isRecensito", isRecensito);
        return "book";
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }


    @GetMapping("/admin/addBook")
    public String addBook(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("book", new Book());
        return "admin/formNewBook";
    }


    @PostMapping("/admin/addBook")
    public String saveBook(@Valid  @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           @RequestParam(value = "authorIds", required = false) String authorIds,
                           @RequestParam("photo") MultipartFile photo,
                           @RequestParam("images") MultipartFile[] images,
                           RedirectAttributes redirectAttributes,
                           Model model) throws IOException {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("book", book);
            return "admin/formNewBook";
        }


        List<Author> selectedAuthors = new ArrayList<>();
        if (!authorIds.isEmpty()) {
            selectedAuthors = Arrays.stream(authorIds.split(","))
                    .map(Long::parseLong)
                    .map(authorService::findById)
                    .collect(Collectors.toList());
        }

        try {
            bookService.registerBook(book, photo, images, selectedAuthors);
        } catch (IOException e) {
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("success", "Libro aggiunto con successo!");
        return "redirect:/book/" + book.getId();
    }

    @GetMapping("/admin/editBook/{book_id}")
    public String editAuthor(Model model,
                             @PathVariable Long book_id) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("book", bookService.findById(book_id));
        return "admin/formEditBook";
    }


    @PostMapping("/admin/editBook/{book_id}")
    public String updateAuthor(@Valid @ModelAttribute("book") Book book,
                               BindingResult bindingResult,
                               @RequestParam("photo") MultipartFile photo,
                               @RequestParam("images") MultipartFile[] images,
                               @RequestParam(value = "authorIds", required = false) String authorIds,
                               @PathVariable Long book_id,
                               RedirectAttributes redirectAttributes,
                               Model model) throws IOException {

        Book book1 = bookService.findById(book_id);
        if(book.getTitle() != null && !book.getTitle().equals(book1.getTitle()) && book.getPublicationYear() != null && !book.getPublicationYear().equals(book1.getPublicationYear())) {
            bookValidator.validate(book, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("book_id", book_id);
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("book", book);
            return "admin/formEditBook";
        }

        List<Author> selectedAuthors = new ArrayList<>();
        if (!authorIds.isEmpty()) {
            selectedAuthors = Arrays.stream(authorIds.split(","))
                    .map(Long::parseLong)
                    .map(authorService::findById)
                    .collect(Collectors.toList());
        }

        book1.setTitle(book.getTitle());
        book1.setPublicationYear(book.getPublicationYear());

        try {
            bookService.registerBook(book1, photo, images, selectedAuthors);
        } catch (IOException e) {
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("success", "Autore modificato con successo!");
        return "redirect:/book/" + book_id;
    }


    @GetMapping("admin/editBook/{book_id}/deleteAuthor/{author_id}")
    public String removeBookFromAuthor(@PathVariable Long author_id,
                                       @PathVariable Long book_id,
                                       Model model) {
        bookService.removeAuthor(book_id, author_id);
        return "redirect:/admin/editBook/" + book_id;
    }

    @GetMapping("admin/deleteBook/{book_id}")
    public String removeBook(@PathVariable Long book_id) {
        bookService.deleteBook(book_id);
        return "redirect:/books";
    }

    @GetMapping("/admin/editBook/deletePhoto/{book_id}")
    public String removePhoto(@PathVariable Long book_id,
                              Model model) {
        Book book = bookService.findById(book_id);
        Long image_id = book.getCover().getId();
        book.setCover(null);
        imageService.deleteImage(image_id);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("book", book);
        return "redirect:/admin/editBook/" + book_id;
    }
}
