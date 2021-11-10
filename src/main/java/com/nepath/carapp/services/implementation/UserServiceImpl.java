package com.nepath.carapp.services.implementation;

import com.nepath.carapp.dtos.input.LoginDto;
import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.Role;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.RoleRepository;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User getUser(String userName) {
        User user = userRepository.findByNick(userName);
        if(user == null) {
            throw new ApiRequestException.NotFoundErrorException("User Not found");
        }
        return user;
    }

    @Override
    public UserCarsDto getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new ApiRequestException.NotFoundErrorException("User Not found");
        } else {
            return userMapper.userToUserCarsDto(user.get());
        }
    }

    @Override
    public User saveUser(UserCreateDto userCreateDto) {
        User user = userMapper.createUserToUser(userCreateDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setId(2L);
        user.setRole(role);
        if(userRepository.existsUserByEmail(user.getEmail())) {
            throw new ApiRequestException.ConflictException("Email already exists");
        }
        if(userRepository.existsUserByNick(user.getNick())) {
            throw new ApiRequestException.ConflictException("Nick already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByNick(username);
        if(user == null) {
            throw new ApiRequestException.NotFoundErrorException("User does not exist");
        }
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new ApiRequestException.NotFoundErrorException("Role does not exist");
        }
        user.setRole(role);
    }

    @Override
    public List<UserDto> getUsers() {
        return userMapper.userToUserDto(userRepository.findAll());
    }
}
