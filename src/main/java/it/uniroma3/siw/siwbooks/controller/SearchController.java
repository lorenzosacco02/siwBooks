package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.model.Author;
import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.service.AuthorService;
import it.uniroma3.siw.siwbooks.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    BookService bookService;
    @Autowired
    AuthorService authorService;

    @GetMapping("/search")
    public String search(
            @RequestParam("query") String query,
            @RequestParam(value = "type", required = false) String type,
            Model model) {

        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();

        if (type == null || type.isEmpty()) {
            // Cerca sia libri che autori
            books = bookService.searchByTitle(query);
            authors = authorService.searchByName(query);
        } else if ("books".equals(type)) {
            books = bookService.searchByTitle(query);
        } else if ("authors".equals(type)) {
            authors = authorService.searchByName(query);
        }

        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("query", query);
        model.addAttribute("type", type);

        return "searchResults"; // nome della pagina con i risultati
    }
}
