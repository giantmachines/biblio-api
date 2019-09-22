package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.cj.protocol.ColumnDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @OneToOne
    private Author author;
    private String image;    // An image path or URL
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @OrderBy("timeCreated desc")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(12) default 'AVAILABLE'")
    @Builder.Default
    private Status status = Status.AVAILABLE;
    @OneToOne(fetch = FetchType.LAZY)
    private User lastModifiedBy;
    @LastModifiedDate
    private Long lastModifiedAt;
}
