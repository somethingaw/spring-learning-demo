package com.sfaw.springsecurityknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
// 接口权限
// @PreAuthorize("hasPermission('user', 'read') or hasRole('ROLE_ADMIN')")
public class UserController {

	@Operation(summary = "获取用户", description = "需要ADMIN权限才能访问", security = { @SecurityRequirement(name = "oauth", scopes = "read") })
	@Parameters({
			@Parameter(name = "id", description = "主键"),
			@Parameter(name = "name", description = "名称", required = false),
			@Parameter(name = "age", description = "年龄", required = true),
			@Parameter(name = "class", description = "班级")
	})
	@GetMapping("/get")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String getUser(@RequestParam(name = "id", required = false) String id) {
		return "It is a user";
	}

	@PostMapping("/add")
	@PreAuthorize("hasPermission('USER', 'update')")
	public String addUser() {
		return "add user success";
    }

	@Operation(summary = "更新用户", description = "需要USER权限才能访问", security = { @SecurityRequirement(name = "oauth") })
    // 使用自带的 需要加前缀 ROLE_ （SecurityExpressionRoot）, 或者自己写一个bean进行校验
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/update")
    public String updateUser() {
        return "update user success";
    }
}
