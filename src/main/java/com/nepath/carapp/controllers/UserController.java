package com.nepath.carapp.controllers;

import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/test")
    public User getUser() {
        Optional<User> user =  userRepository.findById(1L);

        return user.orElseGet(() -> new User());
        //return userRepository.findById(1L);
    }
}
