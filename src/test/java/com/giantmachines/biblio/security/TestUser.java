package com.giantmachines.biblio.security;

public class TestUser extends CurrentUser {
    @Override
    public String get() {
        return "paford@gmail.com";
    }
}
