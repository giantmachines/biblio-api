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
    private Long timeCreated;
    @Column(name = "last_updated")
    private Long timeUpdated;

    @PrePersist
    private void setTimeCreated(){
        this.timeCreated = this.timeCreated == null ? new Date().getTime() : this.timeCreated;
        this.timeUpdated = new Date().getTime();
    }

    public Review(User reviewer, int value, String comments) {
        this.reviewer = reviewer;
        this.value = value;
        this.comments = comments;
        this.timeCreated = new Date().getTime();
        this.timeUpdated = new Date().getTime();
    }

    public Review(long id, User reviewer, int value, String comments) {
        this(reviewer, value, comments);
        this.id = id;
    }

    public Review(ReviewBuilder builder){
        this.id = builder.id;
        this.reviewer = builder.reviewer;
        this.comments = builder.comments;
        this.value = builder.value;
        this.timeCreated = builder.timeCreated;
        this.timeUpdated = builder.timeUpdated;
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

    public long getTimeCreated() {
        return timeCreated;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public static class ReviewBuilder{
        private long id;
        private User reviewer;
        private int value;
        private String comments;
        private Long timeCreated;
        private Long timeUpdated;

        public ReviewBuilder(Review review) {
            this.id = review.id;
            this.reviewer = review.reviewer;
            this.value = review.value;
            this.comments = review.comments;
            this.timeCreated = review.timeCreated;
            this.timeUpdated = review.timeUpdated;
        }

        public ReviewBuilder setValue(int value) {
            this.value = value;
            return this;
        }

        public ReviewBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }
    }
}
