package com.sfaw.springsecurityknife.service;

import com.sfaw.springsecurityknife.dto.AuthRequestDTO;
import com.sfaw.springsecurityknife.dto.AuthResponseDTO;
import com.sfaw.springsecurityknife.dto.RegisterRequestDTO;
import com.sfaw.springsecurityknife.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthService
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 10:16
 **/
@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        User person = new User();
        person.setName(dto.getName());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person = userService.create(person);
        return new AuthResponseDTO(jwtService.generateToken(person.getName()));
    }

    public AuthResponseDTO authenticate(AuthRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getName(),
                        dto.getPassword()));
        final User person = userService.findByName(dto.getName());
        return new AuthResponseDTO(jwtService.generateToken(person.getName()));
    }

    public AuthResponseDTO refreshToken(HttpServletRequest request) {
        String token = jwtService.getTokenFromReq(request);
        return new AuthResponseDTO(jwtService.refreshToken(token));
    }
}
