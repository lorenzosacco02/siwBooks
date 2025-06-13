package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Review;
import it.uniroma3.siw.siwbooks.model.User;
import it.uniroma3.siw.siwbooks.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public Review findById(Long id) {
        return reviewRepository.findById(id).get();
    }

    @Transactional
    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Transactional
    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Transactional
    public List<Review> getReviewsForBook(Book book) {
        return reviewRepository.findByBook(book);
    }

    @Transactional
    public void update(Long reviewId, String title, String text, Integer rating){
        Review review = this.findById(reviewId);
        review.setTitle(title);
        review.setText(text);
        review.setRating(rating);
        save(review);
    }

    @Transactional
    public boolean userHasAlreadyReviewed(Book book, User user) {
        return reviewRepository.existsByBookAndAuthor(book, user);
    }
}
