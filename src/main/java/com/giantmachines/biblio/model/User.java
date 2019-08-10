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

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(UserBuilder builder){
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.active = builder.active;
        this.online = builder.online;
    }

    public User() { }

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


    public static class UserBuilder {
        private long id;
        private String firstName;
        private String lastName;
        private boolean active;
        private boolean online;

        public UserBuilder(User user) {
            this.id = user.id;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.active = user.active;
            this.online = user.online;
        }

        public UserBuilder setActive(boolean active){
            this.active = active;
            return this;
        }

        public UserBuilder setOnline(boolean online){
            this.online = online;
            return this;
        }
    }
}
