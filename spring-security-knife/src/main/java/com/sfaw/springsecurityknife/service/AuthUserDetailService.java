package com.sfaw.springsecurityknife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * AuthUserDetailService
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:33
 **/
@Service
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private UserService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return personService.findByName(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
