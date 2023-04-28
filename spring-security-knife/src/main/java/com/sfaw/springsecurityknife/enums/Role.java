package com.sfaw.springsecurityknife.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * Role
 * 角色信息
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:56
 **/
public enum Role {

    USER(1, "User"),
    ADMIN(2, "Administrator");

    private Integer id;
    private String description;

    Role(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public static Role fromId(Integer id) {
        return Arrays.stream(Role.values())
                .filter(role -> Objects.equals(role.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + id));
    }

    public static Role fromDescription(String description) {
        return Arrays.stream(Role.values())
                .filter(role -> Objects.equals(role.getDescription(), description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + description));
    }
}
