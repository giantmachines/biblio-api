package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getByUserName(s);

        if (user == null){
            throw new UsernameNotFoundException(s);
        }

        log.info(String.format("User %s was authenticated.", s));
        return new UserPrincipal(user);
    }
}
