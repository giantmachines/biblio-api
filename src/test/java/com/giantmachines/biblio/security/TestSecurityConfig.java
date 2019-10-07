package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("test")
public class TestSecurityConfig {

    @Bean
    AuditorAware<User> auditorProvider() {
        return () -> Optional.ofNullable(User
                .builder()
                .id(1L)
                .email("paford@gmail.com")
                .build());
    }
}
