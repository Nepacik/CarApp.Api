package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.UserCreateDto;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.mappers.CarMapper;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.CarRepository;
import com.nepath.carapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CarMapper carMapper;
    private final CarRepository carRepository;
    private final TestService testService;

    @GetMapping("/test")
    public UserCarsDto getUser() {
        Optional<User> user =  userRepository.findById(1L);
        return userMapper.userToUserCarsDto(user.orElseGet(() -> new User()));
    }

    @GetMapping("/test1")
    public List<UserDto> getUsers() {
        List<User> users =  userRepository.findAll();
        ArrayList<User> arrayList = new ArrayList<>(users);
        arrayList.add(users.get(0));
        return userMapper.userToUserDto(arrayList);
    }

    @GetMapping("/test3")
    public List<CarDto> getAllCars() {
        testService.a = testService.a + 100;
        return carMapper.carToCarDto(carRepository.findAll());
    }

    @GetMapping("/test4")
    public String pso() {
        testService.a = testService.a + 100;
        return String.valueOf(testService.a);
    }

//    @PostMapping("/createUser")
//    public ResponseEntity<String> xd(@Valid @RequestBody UserCreateDto userCreateDto) {
//        User user = userMapper.createUserToUser(userCreateDto);
//        userRepository.save(user);
//        return ResponseEntity<String>("ok",HttpStatus.CREATED);
//    }
}
