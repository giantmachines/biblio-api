package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Column(columnDefinition = "tinyint default 1")
    private boolean active = true;
    @Column(columnDefinition = "tinyint default 0")
    private boolean online = false;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
}
