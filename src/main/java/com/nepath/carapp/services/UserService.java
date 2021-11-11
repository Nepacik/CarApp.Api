package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.PaginationClassDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.models.User;

public interface UserService {
    User getUser(String userName);

    UserCarsDto getUser(Long id);

    void saveUser(UserCreateDto userCreateDto);

    PaginationClassDto<UserDto> getUsers(int page);

    void deleteUser();
}
