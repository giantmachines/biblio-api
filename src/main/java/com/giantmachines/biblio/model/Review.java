package com.giantmachines.biblio.model;

import javax.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User reviewer;
    private int value;
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
