package com.giantmachines.biblio.security;

import com.giantmachines.biblio.model.User;
import com.giantmachines.biblio.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getByUserName(s);

        if (user == null){
            throw new UsernameNotFoundException(s);
        }

        return new UserPrincipal(user);
    }
}
