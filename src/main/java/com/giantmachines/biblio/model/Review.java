package com.giantmachines.biblio.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.giantmachines.biblio.serializers.ReviewSerializer;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonSerialize(using = ReviewSerializer.class)
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
    private long reviewTime;

    @PrePersist
    private void addReviewTime(){
        this.reviewTime = new Date().getTime();
    }

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

    public long getReviewTime() {
        return reviewTime;
    }
}
