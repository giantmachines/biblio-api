package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private transient User user;
    private static final long serialVersionUID = 4L;

    private List<GrantedAuthority> authorities = new ArrayList<>();
    {
        authorities.add((GrantedAuthority) () -> "ROLE_USER");
    }

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isOnline();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    public User getUser(){
        return user;
    }
}
