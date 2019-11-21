package com.giantmachines.biblio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @OneToOne
    private Author author;
    private String image;    // An image path or URL
    private String description;
    private String publisher;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "varchar(12) default 'AVAILABLE'")
    @Builder.Default
    private Status status = Status.AVAILABLE;
    @Column(name = "time_created", nullable = false, updatable = false)
    @CreatedDate
    private long timeCreated;
    @OneToOne(fetch = FetchType.EAGER)
    @LastModifiedBy
    private User lastModifiedBy;
    @Column(name = "last_updated", nullable = false)
    @LastModifiedDate
    private long timeUpdated;
}
