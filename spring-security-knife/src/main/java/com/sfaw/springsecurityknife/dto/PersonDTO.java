package com.sfaw.springsecurityknife.dto;

import com.sfaw.springsecurityknife.entity.User;
import com.sfaw.springsecurityknife.enums.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PersonDTO
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:55
 **/
public class PersonDTO {

    private Long id;
    private String name;
    private String password;
    private Set<String> roles = new HashSet<>();

    public PersonDTO() {
        super();
    }

    public PersonDTO(Long id, String name, Set<String> roles) {
        super();
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public PersonDTO(User person) {
        super();
        this.id = person.getId();
        this.name = person.getName();
        this.setRoles(person.getRoles());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles.stream().map(r -> r.getDescription()).collect(Collectors.toSet());
    }
}
