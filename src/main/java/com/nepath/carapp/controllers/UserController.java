package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.output.PaginationClassDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.UserMapper;
import com.nepath.carapp.security.JWTExtensions;
import com.nepath.carapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PaginationClassDto<UserDto>> getUsers(@RequestParam int page) {
        PaginationClassDto<UserDto> userDtos = userService.getUsers(page);
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping("/userCar")
    public ResponseEntity<UserCarsDto> getUserCars(@RequestParam Long id) {
        UserCarsDto userCarsDto = userService.getUser(id);
        return ResponseEntity.ok().body(userCarsDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
