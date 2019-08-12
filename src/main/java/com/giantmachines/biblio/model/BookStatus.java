package com.giantmachines.biblio.model;

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

    public BookStatus(Status value, User user) {
        this(value);
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
}

