package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public CurrentUser currentUser(){
        return new TestUser();
    }

}
