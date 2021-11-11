package com.nepath.carapp.services.implementation;

import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.PaginationClassDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.Role;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.CarRepository;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.security.extensions.CurrentUser;
import com.nepath.carapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
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
    public void saveUser(UserCreateDto userCreateDto) {
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
        userRepository.save(user);
    }

    @Override
    public PaginationClassDto<UserDto> getUsers(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<User> users = userRepository.findAllUsersSortByEmail(pageable);
        return new PaginationClassDto<>(userMapper.userToUserDto(users.getContent()), page);
    }

    @SneakyThrows
    @Transactional
    @Async
    @Override
    public void deleteUser() {
        carRepository.removeCarOwner(CurrentUser.getUserId());
        TimeUnit.SECONDS.sleep(20);
        userRepository.deleteById(CurrentUser.getUserId());
    }
}
