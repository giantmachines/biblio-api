package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

    public BookStatus(Status value, Book book, User user) {
        this(value);
        this.book = book;
        this.user = user;
    }

    public BookStatus(Status value) {
        this.value = value;
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
}

