package com.nepath.carapp.controllers;

import com.nepath.carapp.dtos.input.*;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.mappers.BrandMapper;
import com.nepath.carapp.mappers.CarMapper;
import com.nepath.carapp.mappers.EngineMapper;
import com.nepath.carapp.mappers.ModelMapper;
import com.nepath.carapp.models.Car;
import com.nepath.carapp.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminToolsController {
    private final AdminService adminService;
    private final CarMapper carMapper;
    private final EngineMapper engineMapper;
    private final ModelMapper modelMapper;
    private final BrandMapper brandMapper;

    @PostMapping("/createEngine")
    ResponseEntity<Void>createEngine(@Valid @RequestBody EngineCreateDto engineCreateDto) {
        adminService.createEngine(engineMapper.engineDtoToEngine(engineCreateDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createModel")
    ResponseEntity<Void>createModel(@Valid @RequestBody ModelCreateDto modelCreateDto) {
        adminService.createModel(modelMapper.modelDtoToModel(modelCreateDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createBrand")
    ResponseEntity<Void>createBrand(@Valid @RequestBody BrandCreateDto brandCreateDto) {
        adminService.createBrand(brandMapper.brandDtoToBrand(brandCreateDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeCarOwner")
    ResponseEntity<CarDto> changeCarOwner(@Valid @RequestBody ChangeCarOwnerDto changeCarOwnerDto) {
        adminService.changeCarOwner(changeCarOwnerDto.getCarId(), changeCarOwnerDto.getNewOwnerId());
        return ResponseEntity.ok().build();
    }
}
