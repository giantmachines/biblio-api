package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    @Autowired
    private UserService userService;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        User user = userService.getByUserName(authentication.getName());
        return Optional.ofNullable(user);
    }
}
