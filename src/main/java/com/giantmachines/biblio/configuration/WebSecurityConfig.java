package com.giantmachines.biblio.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.security.JwtTokenFilter;
import com.giantmachines.biblio.security.JwtTokenProvider;
import com.giantmachines.biblio.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({"secure", "production"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private static final String LOGIN_PATH = "/users/login";


    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = tokenProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .and()
                .authenticationProvider(authenticationProvider())
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint())
                .and()
                .formLogin()
                    .permitAll()
                    .loginProcessingUrl(LOGIN_PATH)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler())
                .and()
                .logout()
                    .permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher(LOGIN_PATH, "DELETE"))
                    .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .anonymous();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public PasswordEncoder encoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("bcrypt"));
        return passwordEncoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationEntryPoint entryPoint(){
        return (req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler successHandler(){
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = ((UserPrincipal) auth.getPrincipal()).getUser();
                user = user.toBuilder().password(null).build();
                String token = jwtTokenProvider.createToken(user.getEmail());
                resp.setHeader("Authorization", "Bearer " + token);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(user));
                resp.getWriter().flush();
            }
        };
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler(){
        return new SimpleUrlAuthenticationFailureHandler(){
            @Override
            public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setHeader("Authorization", null);
                resp.getWriter().flush();
            }
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return (req, resp, auth) -> {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().flush();
        };
    }
}
