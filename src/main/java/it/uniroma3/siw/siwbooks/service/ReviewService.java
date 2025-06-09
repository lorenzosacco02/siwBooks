package it.uniroma3.siw.siwbooks.service;

import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.model.Review;
import it.uniroma3.siw.siwbooks.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id).get();
    }

    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void save(Review movie) {
        reviewRepository.save(movie);
    }

    public List<Review> getReviewsForBook(Book book) {
        return reviewRepository.findByBook(book);
    }
}
