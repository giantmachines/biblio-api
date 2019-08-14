package com.giantmachines.biblio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    private Author author;
    private String image;    // An image path or URL
    @OneToMany(fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private BookStatus status = new BookStatus();

    public Book(String title, Author author, String image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public Book(String title, Author author, String image, List<Review> reviews, BookStatus status) {
        this(title, author, image);
        this.reviews = reviews;
        this.status = status;
    }

    public Book(BookBuilder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.status = builder.status;
        this.image = builder.image;
        this.id = builder.id;
        this.reviews = builder.reviews;
    }

    public Book() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public BookStatus getStatus() {
        return status;
    }

    public static class BookBuilder {
        private long id;
        private String title;
        private Author author;
        private String image;
        private List<Review> reviews;
        private BookStatus status;

        public BookBuilder(Book book) {
            this.id = book.id;
            this.title = book.title;
            this.author = book.author;
            this.image = book.image;
            this.reviews = book.reviews;
            this.status = book.status;
        }

        public BookBuilder setReviews(List<Review> reviews) {
            this.reviews = reviews;
            return this;
        }

        public BookBuilder setStatus(BookStatus status) {
            this.status = status;
            return this;
        }
    }
}
