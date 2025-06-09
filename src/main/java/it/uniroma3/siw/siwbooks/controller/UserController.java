package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getAllBooks(Model model) {
        model.addAttribute("user" ,userService.getCurrentUser());
        return "profile";
    }

}
