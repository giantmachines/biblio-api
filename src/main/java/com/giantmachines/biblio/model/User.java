package com.giantmachines.biblio.model;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    @Column(columnDefinition = "tinyint default 0")
    private boolean active = true;
    @Column(columnDefinition = "tinyint default 0")
    private boolean online = false;

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isOnline() {
        return online;
    }
}
