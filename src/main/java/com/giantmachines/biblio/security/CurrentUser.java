package com.giantmachines.biblio.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provides a way to look up the currently-logged in user form the Spring Security context.
 */
public class CurrentUser{
    public String get(){
        String userName = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)){
            userName = auth.getName();
        }
        return userName;
    }
}
