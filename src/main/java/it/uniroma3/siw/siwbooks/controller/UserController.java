package it.uniroma3.siw.siwbooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/profile")
    public String getAllBooks(Model model) {
        return "profile";
    }

}
