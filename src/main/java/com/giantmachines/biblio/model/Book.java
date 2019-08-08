package com.giantmachines.biblio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @ManyToOne
    private Author author;
    private String image;    // An image path
    @OneToMany
    private List<Review> reviews = new ArrayList<>();

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
}
