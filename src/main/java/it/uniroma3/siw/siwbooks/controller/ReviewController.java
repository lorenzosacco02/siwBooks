package it.uniroma3.siw.siwbooks.controller;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Credentials;
import it.uniroma3.siw.siwbooks.model.Review;
import it.uniroma3.siw.siwbooks.model.User;
import it.uniroma3.siw.siwbooks.service.BookService;
import it.uniroma3.siw.siwbooks.service.ReviewService;
import it.uniroma3.siw.siwbooks.service.UserService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/book/{book_id}/reviews")
    public String addReview(@PathVariable("book_id") Long bookId, Model model) {
        model.addAttribute("book", bookService.findById(bookId));
        return "formNewReview";
    }

    @PostMapping("/book/{book_id}/reviews")
    public String saveReview(@PathVariable("book_id") Long bookId,
                            @Valid @ModelAttribute("review") Review review,
                            BindingResult bindingResult,
                            Model model, Principal principal) {

        Book book = bookService.findById(bookId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("reviews", reviewService.getReviewsForBook(book));
            return "bookDetails";
        }

        review.setAuthor(userService.getCurrentUser());
        review.setBook(book);

        reviewService.save(review);

        return "redirect:/book/" + bookId;
    }
}