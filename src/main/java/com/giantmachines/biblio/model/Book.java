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
    @ManyToOne
    private Author author;
    private String image;    // An image path or URL
    @OneToMany
    private List<Review> reviews = new ArrayList<>();
    @OneToOne
    private BookStatus status = new BookStatus();

    public Book(String title, Author author, String image, List<Review> reviews, BookStatus status) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.reviews = reviews;
        this.status = status;
    }

    public Book() { }

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
}
