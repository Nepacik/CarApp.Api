package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = CarMapper.class)
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    List<UserDto> userToUserDto(List<User> user);
}
