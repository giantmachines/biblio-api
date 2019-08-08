package com.giantmachines.biblio.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User reviewer;
    private int value;

    public long getId() {
        return id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public int getValue() {
        return value;
    }
}
