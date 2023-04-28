package com.sfaw.springsecurityknife.entity;

import com.sfaw.springsecurityknife.dto.PersonDTO;
import com.sfaw.springsecurityknife.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Person
 * 用户信息 ,需要实现 UserDetails ，可拓展
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 10:00
 **/
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String password;

    private Set<Integer> roles = new HashSet<>(Collections.singletonList(Role.USER.getId()));

    public User() {
        super();
    }

    public User(Long id, String name, String password, Set<Role> roles) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.setRoles(roles);
    }

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public User(PersonDTO dto) {
        this(dto.getName(), dto.getPassword());
        this.setId(dto.getId());
        this.setStringRoles(dto.getRoles());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles.stream().map(r -> Role.fromId(r)).collect(Collectors.toSet());
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
        if (roles == null || roles.isEmpty()) {
            this.roles.clear();
        } else {
            this.roles = roles.stream().map(r -> r.getId()).collect(Collectors.toSet());
        }
    }

    public void setStringRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            this.roles.clear();
        } else {
            this.roles = roles.stream().map(s -> Role.fromDescription(s).getId()).collect(Collectors.toSet());
        }
    }

    public void addRole(Role role) {
        this.roles.add(role.getId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(Role.fromId(r).name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", roles=" + getRoles() + "]";
    }
}
