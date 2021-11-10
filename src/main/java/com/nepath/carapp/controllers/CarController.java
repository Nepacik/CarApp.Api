package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.CarCreateDto;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.mappers.CarMapper;
import com.nepath.carapp.models.Car;
import com.nepath.carapp.models.User;
import com.nepath.carapp.security.CurrentUser;
import com.nepath.carapp.security.SecurityUserDetails;
import com.nepath.carapp.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/car")
@RequiredArgsConstructor
@RestController
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> registerCar(@Valid @RequestBody CarCreateDto carCreateDto) {
        Car car = carMapper.carCreateDtoToCar(carCreateDto);
        car.setUser(CurrentUser.getUserWithOnlyIdSet());
        carService.registerCar(car);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok().body(carMapper.carToCarDto(carService.getAllCars()));
    }
}
