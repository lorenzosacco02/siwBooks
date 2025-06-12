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
import java.util.List;

@Controller
public class BookController {

    @Autowired
    ImageService imageService;

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;

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
        return "book";
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }


    @GetMapping("/admin/formNewBook/addAuthor/{id}")
    public String getAllAuthors(Model model,
                              HttpSession session,
                              @PathVariable Long id) {
        Book book = (Book) session.getAttribute("book");
        Author author = authorService.findById(id);
        List<Author> addedAuthors = book.getAuthors();
        if (!addedAuthors.contains(author)) {
            book.getAuthors().add(author);
        }
        model.addAttribute("book", book);
        List<Author> newAuthors = (List<Author>)authorService.findAll();
        newAuthors.removeAll(addedAuthors);
        model.addAttribute("authors", newAuthors);
        return "admin/formNewBook";
    }

    @GetMapping("/admin/addBook")
    public String addBook(Model model,
                          HttpSession session) {
        Book book = (Book) session.getAttribute("book");
        if (book == null){
            book = new Book();
        }
        session.setAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("book", new Book());
        return "admin/formNewBook";
    }


    @PostMapping("/admin/addBook")
    public String saveBook(@ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("photo") MultipartFile photo,
                             @RequestParam("images") MultipartFile[] images,
                             Model model) throws IOException {

//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errors", bindingResult.getAllErrors());
//            model.addAttribute("book", book);
//            return "admin/formNewBook";
//        }

        try{
            bookService.registerBook(book, photo, images);
        }
        catch(IOException e){
            return "redirect:/";
        }

        session.removeAttribute("book");
        redirectAttributes.addFlashAttribute("success", "Libro aggiunto con successo!");
        return "redirect:/book/" + book.getId();
    }

}
