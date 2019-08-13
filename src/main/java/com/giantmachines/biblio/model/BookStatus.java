package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_status")
public class BookStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.ORDINAL)
    private Status value = Status.AVAILABLE;
    @ManyToOne
    private User user;
    @OneToOne
    @JsonIgnore
    private Book book;
    private Long lastUpdated;
    @Column(columnDefinition = "tinyint default 1")
    @JsonIgnore
    private boolean latest = true;
    // NOTE:  The use of latest as a flag means that we hae to set previous status records to false,
    // but I want to retrieve from the database only the latest status with each book, and so far this is
    // the least bad way to do it.


    public BookStatus(Status value, Book book, User user) {
        this(value, book);
        this.user = user;
    }

    public BookStatus(Status value, Book book) {
        this.value = value;
        this.book = book;
        this.lastUpdated = new Date().getTime();
        this.latest = true;
    }

    public BookStatus(BookStatus that){
        this(that.value, that.book, that.user);
        this.latest = false;
        this.id = that.id;
    }

    public BookStatus() {
    }

    public long getId() {
        return id;
    }

    public Status getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public boolean isLatest() {
        return latest;
    }
}

