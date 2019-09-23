package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private CurrentUser currentUser;

    @Override
    public Optional<String> getCurrentAuditor() {
        /*
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserPrincipal.class::cast)
                .map(UserPrincipal::getUser);
         */
        return Optional.ofNullable(currentUser.get());
    }
}
