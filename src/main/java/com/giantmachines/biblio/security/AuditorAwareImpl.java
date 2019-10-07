package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName().equals("anonymous")
                || authentication.getName().equals("anonymousUser")) {
            return Optional.empty();
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return Optional.ofNullable(principal.getUser());
    }
}
