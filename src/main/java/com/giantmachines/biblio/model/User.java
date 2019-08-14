package com.giantmachines.biblio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
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
    private String email;
    @JsonIgnore
    private String password;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(UserBuilder builder){
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.active = builder.active;
        this.online = builder.online;
        this.email = builder.email;
        this.password = builder.password;
    }

    public User() { }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public  String getPassword(){
        return this.password;
    }


    public static class UserBuilder {
        private long id;
        private String firstName;
        private String lastName;
        private boolean active;
        private boolean online;
        private String email;
        private String password;

        public UserBuilder(User user) {
            this.id = user.id;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.active = user.active;
            this.online = user.online;
            this.email = user.email;
            this.password = user.password;
        }

        public UserBuilder setActive(boolean active){
            this.active = active;
            return this;
        }

        public UserBuilder setOnline(boolean online){
            this.online = online;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
