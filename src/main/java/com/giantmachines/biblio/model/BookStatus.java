package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_status")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class BookStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.ORDINAL)
    private Status value = Status.AVAILABLE;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Book book;
    private Long lastUpdated;
    @Column(columnDefinition = "tinyint default 1")
    @JsonIgnore
    @Builder.Default
    private boolean latest = true;
    // NOTE:  The use of latest as a flag means that we hae to set previous status records to false,
    // but I want to retrieve from the database only the latest status with each book, and so far this is
    // the least bad way to do it.

    @PrePersist
    private void addUpdateTime(){
        this.lastUpdated = new Date().getTime();
    }
}

