package it.uniroma3.siw.siwbooks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"author_id", "book_id"})
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Specifica un titolo")
    private String title;

    @Min(value = 0)
    @Max(value = 5)
    @NotNull(message = "Specifica un voto")
    private Integer rating; // 1-5

    @Column(length = 500)
    private String text;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User author;

    private String authorUsername;

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(getTitle(), review.getTitle()) && Objects.equals(getBook(), review.getBook()) && Objects.equals(getAuthor(), review.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getBook(), getAuthor());
    }
}
