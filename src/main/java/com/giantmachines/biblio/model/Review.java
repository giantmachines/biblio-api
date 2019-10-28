package com.giantmachines.biblio.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.giantmachines.biblio.serializers.ReviewSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;


@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonSerialize(using = ReviewSerializer.class)
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.EAGER)
    private User reviewer;
    @NonNull
    private int value;
    private String comments;
    @Column(name = "time_created", nullable = false, updatable = false)
    @CreatedDate
    private long timeCreated;
    @Column(name = "last_updated", nullable = false)
    @LastModifiedDate
    private long timeUpdated;
}
