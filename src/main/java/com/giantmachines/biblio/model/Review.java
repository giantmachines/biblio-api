package com.giantmachines.biblio.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.giantmachines.biblio.serializers.ReviewSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonSerialize(using = ReviewSerializer.class)
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
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
}
