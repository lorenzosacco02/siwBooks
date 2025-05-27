package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
}
