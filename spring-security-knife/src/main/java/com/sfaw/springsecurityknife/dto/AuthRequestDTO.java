package com.sfaw.springsecurityknife.dto;

/**
 * AuthRequestDTO
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:54
 **/
public class AuthRequestDTO {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
