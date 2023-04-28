package com.sfaw.springsecurityknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User
 * 测试接口
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/18 15:53
 **/
@RestController
@RequestMapping("/user")
@Tag(name = "用户信息")
public class UserController {

    @Operation(summary = "获取用户", description = "123", security = {@SecurityRequirement(name = "oauth", scopes = "read")})
    @GetMapping("/get")
    public String getUser() {
        return "It is a user";
    }

    @PostMapping("/add")
    public String addUser() {
        return "add user success";
    }

    @Operation(summary = "更新用户", description = "123", security = {@SecurityRequirement(name = "oauth")})
    @PostMapping("/update")
    public String updateUser() {
        return "update user success";
    }
}
