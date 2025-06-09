package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.model.Credentials;
import it.uniroma3.siw.siwbooks.model.User;
import it.uniroma3.siw.siwbooks.service.CredentialsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/accessDenied")
        public String accessDenied() {
            return "accessDenied";
        }

        @GetMapping(value = "/register")
        public String showRegisterForm (Model model) {
            model.addAttribute("user", new User());
            model.addAttribute("credentials", new Credentials());
            return "formRegisterUser";
        }

        @GetMapping(value = "/login")
        public String showLoginForm (Model model) {
            return "formLogin";
        }

        @GetMapping(value = "/")
        public String index(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                return "index";
            }
            else {
                UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
                if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                    return "admin/indexAdmin";
                }
            }
            return "index";
        }

        @GetMapping(value = "/success")
        public String defaultAfterLogin(Model model) {

            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin";
            }
            return "index";
        }

    @PostMapping(value = { "/register" })
        public String registerUser(@Valid @ModelAttribute("user") User user,
                                   BindingResult userBindingResult, @Valid
                                   @ModelAttribute("credentials") Credentials credentials,
                                   BindingResult credentialsBindingResult,
                                   Model model) {

            // se user e credential hanno entrambi contenuti validi, memorizza User e Credentials nel DB
            if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
                credentials.setUser(user);
                credentialsService.saveCredentials(credentials);
                model.addAttribute("user", user);
                return "registrationSuccess";
            }
            return "registerUser";
        }
    }

