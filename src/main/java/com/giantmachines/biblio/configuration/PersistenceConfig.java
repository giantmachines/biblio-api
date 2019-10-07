package com.giantmachines.biblio.configuration;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.security.AuditorAwareImpl;
import com.giantmachines.biblio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {

    @Bean
    @Profile({"secure", "production"})
    AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }
}



