package com.nepath.carapp.services;

import com.nepath.carapp.models.User;

import java.util.List;

public interface UserService {
    User getUser(String userName);
    User getUser(Long id);
    User saveUser(User user);
    void addRoleToUser(String username, String roleName);
    List<User> getUsers();
}
