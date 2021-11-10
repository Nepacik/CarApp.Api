package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.models.User;

import java.util.List;

public interface UserService {
    User getUser(String userName);
    UserCarsDto getUser(Long id);
    User saveUser(UserCreateDto userCreateDto);
    void addRoleToUser(String username, String roleName);
    List<UserDto> getUsers();
}
