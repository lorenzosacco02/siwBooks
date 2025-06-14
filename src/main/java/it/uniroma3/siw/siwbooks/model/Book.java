package it.uniroma3.siw.siwbooks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(value= 0, message = "L'anno di Pubblicazione deve essere positivo")
    @Max(value = 2025, message = "L'Anno di Pubblicazione deve essere uguale o precedente a quello attuale")
    @NotNull(message = "Specifica un Anno di Pubblicazione")
    private Integer publicationYear;

    @Column(nullable = false)
    @NotBlank(message = "Specifica il Titolo")
    private String title;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Image cover;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @Column(name = "images")
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

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

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getPublicationYear(), book.getPublicationYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getPublicationYear());
    }
}
