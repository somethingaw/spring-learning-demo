package com.sfaw.springsecurityknife.dto;

/**
 * RegisterRequestDTO
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:57
 **/
public class RegisterRequestDTO {

    private String name;
    private String password;

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }

}
