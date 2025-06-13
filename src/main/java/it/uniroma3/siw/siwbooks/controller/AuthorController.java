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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/admin/addAuthor")
    public String addAuthor(Model model) {
        Author author = new Author();
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("author", author);
        return "admin/formNewAuthor";
    }


    @PostMapping("/admin/addAuthor")
    public String saveAuthor(@ModelAttribute("author") Author author,
                             @RequestParam(value = "bookIds", required = false) String bookIds,
                             @RequestParam("photo") MultipartFile photo,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("author", author);
            return "admin/formNewAuthor";
        }

        List<Book> selectedBooks = new ArrayList<>();
        if (!bookIds.isEmpty()) {
            selectedBooks = Arrays.stream(bookIds.split(","))
                    .map(Long::parseLong)
                    .map(bookService::findById)
                    .collect(Collectors.toList());
        }

        try {
            authorService.registerAuthor(author, photo, selectedBooks);
        } catch (IOException e) {
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("success", "Autore aggiunto con successo!");
        return "redirect:/author/" + author.getId();
    }


    @GetMapping("/admin/editAuthor/{author_id}")
    public String editAuthor(Model model,
                             @PathVariable Long author_id) {
        Author author = authorService.findById(author_id);
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("author", author);
        return "admin/formEditAuthor";
    }


    @PostMapping("/admin/editAuthor/{author_id}")
    public String updateAuthor(@ModelAttribute("author") Author author,
                               @RequestParam("photo") MultipartFile photo,
                               @RequestParam(value = "bookIds", required = false) String bookIds,
                               @PathVariable Long author_id,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("author", author);
            return "admin/formEditAuthor";
        }

        List<Book> selectedBooks = new ArrayList<>();
        if (!(bookIds == null || bookIds.isEmpty())) {
            selectedBooks = Arrays.stream(bookIds.split(","))
                    .map(Long::parseLong)
                    .map(bookService::findById)
                    .collect(Collectors.toList());
        }
        Author author1 = authorService.findById(author_id);

        author1.setName(author.getName());
        author1.setSurname(author.getSurname());
        author1.setDateOfBirth(author.getDateOfBirth());
        author1.setDateOfDeath(author.getDateOfDeath());
        try {
            authorService.registerAuthor(author1, photo, selectedBooks);
        } catch (IOException e) {
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("success", "Autore modificato con successo!");
        return "redirect:/author/" + author_id;
    }

    @GetMapping("admin/editAuthor/{author_id}/deleteBook/{book_id}")
    public String removeBookFromAuthor(@PathVariable Long author_id,
                                       @PathVariable Long book_id,
                                       Model model) {
        authorService.removeBook(author_id, book_id);
        return "redirect:/admin/editAuthor/" + author_id;
    }

    @GetMapping("admin/deleteAuthor/{author_id}")
    public String removeAuthor(@PathVariable Long author_id) {
        authorService.deleteAuthor(author_id);
        return "redirect:/authors";
    }
}
