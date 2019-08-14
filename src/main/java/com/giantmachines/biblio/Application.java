package com.giantmachines.biblio;

import com.giantmachines.biblio.security.CurrentUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
    @Bean
    public PasswordEncoder encoder(){
        PasswordEncoder defaultEncoder = new StandardPasswordEncoder();
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(defaultEncoder);
        return  passwordEncoder;
    }

    /**
     * Allows components to access information about the current user from the Spring Security context.
     * @return The current user, from which components can get the user name when needed.
     */
    @Bean
    @Profile({"dev", "production"})
    public CurrentUser currentUser(){
        return new CurrentUser();
    }

    public static void main(String... args){
        SpringApplication.run(Application.class, args);
    }
}
