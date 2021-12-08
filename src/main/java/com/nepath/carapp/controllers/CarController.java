package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.CarCreateDto;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RequestMapping("/car")
@RequiredArgsConstructor
@RestController
public class CarController {

    private final CarService carService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerCar(@Valid @RequestBody CarCreateDto carCreateDto) {
        carService.registerCar(carCreateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> carDto = carService.getAllCars();
        return ResponseEntity.ok().body(carDto);
    }
}
