package com.nepath.carapp.services.implementation;

import com.nepath.carapp.dtos.input.CarCreateDto;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.mappers.CarMapper;
import com.nepath.carapp.repositories.CarRepository;
import com.nepath.carapp.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public void registerCar(CarCreateDto carCreateDto) {
        carRepository.save(carMapper.carCreateDtoToCar(carCreateDto));
    }

    @Override
    public List<CarDto> getAllCars() {
        return carMapper.carToCarDto(carRepository.findAll());
    }
}
