package com.sfaw.springsecurityknife.service;

import com.sfaw.springsecurityknife.entity.User;
import com.sfaw.springsecurityknife.enums.Role;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PersonService
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:59
 **/
@Service
public class UserService {


    public static Map<Long, User> personMap = new HashMap<>();

    // demo 先预存用户到缓存，
    static {
        User person = new User();
        person.setId(10L);
        person.setName("admin");
        // 如果用作登录用户保存到数据库，需要使用 new BCryptPasswordEncoder().encode("admin") 加密
        person.setPassword("admin");
        person.setRoles(Collections.singleton(Role.ADMIN));
        personMap.put(10L, person);
    }

    public User findById(Long id) {
        if (personMap.containsKey(id)) {
            return personMap.get(id);
        }
        return null;
    }

    public User findByName(String name) {
        return personMap.values().stream().filter(t -> t.getName().equals(name)).findAny().get();
    }

    public User create(User person) {
        person.setId(10L);
        person.addRole(Role.USER);
        return personMap.put(person.getId(), person);
    }
}
