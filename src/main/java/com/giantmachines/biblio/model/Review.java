package com.giantmachines.biblio.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    @OneToOne(fetch = FetchType.EAGER)
    private User reviewer;
    @NonNull
    private int value;
    @NonNull
    private String comments;

    public Review(User reviewer, int value, String comments) {
        this.reviewer = reviewer;
        this.value = value;
        this.comments = comments;
    }

    public Review() { }

    public long getId() {
        return id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public int getValue() {
        return value;
    }

    public String getComments(){
        return comments;
    }
}
