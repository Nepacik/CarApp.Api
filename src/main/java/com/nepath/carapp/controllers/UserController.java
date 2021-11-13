package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.output.PaginationClassDto;
import com.nepath.carapp.dtos.output.UserCarsDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
}
