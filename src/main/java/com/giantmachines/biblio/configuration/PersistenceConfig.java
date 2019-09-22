package com.giantmachines.biblio.configuration;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.security.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {

    @Bean
    AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }
}



