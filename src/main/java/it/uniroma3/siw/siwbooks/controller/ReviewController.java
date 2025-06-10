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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/book/{book_id}/addReview")
    public String addReview(@PathVariable("book_id") Long bookId, Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("book", bookService.findById(bookId));
        return "formNewReview";
    }

    @PostMapping("/book/{book_id}/addReview")
    public String saveReview(@PathVariable("book_id") Long bookId,
                            @Valid @ModelAttribute("review") Review review,
                            BindingResult bindingResult,
                            Model model, Principal principal) {

        Book book = bookService.findById(bookId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("reviews", reviewService.getReviewsForBook(book));
            return "/book"+bookId;
        }

        System.out.println(userService.getCurrentUser());
        review.setAuthor(userService.getCurrentUser());
        review.setBook(book);

        reviewService.save(review);

        return "redirect:/book/" + bookId;
    }


    @GetMapping("/book/{book_id}/updateReview/{review_id}")
    public String updateReview(@PathVariable("book_id") Long bookId, @PathVariable("review_id") Long reviewId, Model model) {
        Review review = reviewService.findById(reviewId);
        if(userService.getCurrentUser() == review.getAuthor()){
            model.addAttribute("review", review.getId());
            model.addAttribute("book", bookService.findById(bookId));
            return "formModifyReview";
        }
        return "accessDenied";
    }

    @PostMapping("/book/{book_id}/updateReview/{review_id}")
    public String updateReview(@PathVariable("book_id") Long bookId,
                               @PathVariable("review_id") Long reviewId,
                               @ModelAttribute("review") Review updatedReview,
                               Principal principal) {
        reviewService.update(reviewId, updatedReview.getTitle(), updatedReview.getText(), updatedReview.getRating());
        return "redirect:/book/" + bookId;
    }

}