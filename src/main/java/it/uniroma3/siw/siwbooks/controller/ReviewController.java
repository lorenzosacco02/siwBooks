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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addReview(@PathVariable("book_id") Long bookId,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        Book book = bookService.findById(bookId);
        User currentUser = userService.getCurrentUser();
        if (reviewService.userHasAlreadyReviewed(book, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Hai già recensito questo libro.");
            return "redirect:/book/" + bookId + "#addReviewButton";
        }
        model.addAttribute("review", new Review());
        model.addAttribute("book", bookService.findById(bookId));
        return "formNewReview";
    }

    @PostMapping("/book/{book_id}/addReview")
    public String saveReview(@PathVariable("book_id") Long bookId,
                             @Valid @ModelAttribute("review") Review review,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model, Principal principal) {

        Book book = bookService.findById(bookId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("reviews", reviewService.getReviewsForBook(book));
            return "/formNewReview";
        }
        User currentUser = userService.getCurrentUser();
        if (reviewService.userHasAlreadyReviewed(book, currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Hai già recensito questo libro.");
            return "redirect:/accessDenied";
        }
        review.setAuthor(userService.getCurrentUser());
        review.setBook(book);

        reviewService.save(review);

        return "redirect:/book/" + bookId;
    }


    @GetMapping("/book/{book_id}/updateReview/{review_id}")
    public String updateReview(@PathVariable("book_id") Long bookId,
                               @PathVariable("review_id") Long reviewId,
                               Model model) {
        Review review = reviewService.findById(reviewId);
        if (userService.getCurrentUser() == review.getAuthor()) {
            model.addAttribute("review", review);
            model.addAttribute("book", bookService.findById(bookId));
            return "formModifyReview";
        }
        return "accessDenied";
    }

    @PostMapping("/book/{book_id}/updateReview/{review_id}")
    public String updateReview(@PathVariable("book_id") Long bookId,
                               @PathVariable("review_id") Long reviewId,
                               @Valid @ModelAttribute("review") Review updatedReview,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("review_id", reviewId);
            model.addAttribute("review", updatedReview);
            model.addAttribute("book", bookService.findById(bookId));
            return "formModifyReview";
        }
        reviewService.update(reviewId, updatedReview.getTitle(), updatedReview.getText(), updatedReview.getRating());
        return "redirect:/book/" + bookId;
    }

    @GetMapping("admin/book/{book_id}/deleteReview/{review_id}")
    public String deleteReview(@PathVariable("review_id") Long review_id,
                               @PathVariable("book_id") Long book_id) {
        reviewService.deleteReview(review_id);
        return "redirect:/book/" + book_id;
    }
}