package com.nepath.carapp.services;

import com.nepath.carapp.models.Car;

import java.util.List;

public interface CarService {
    void registerCar(Car car);
    List<Car> getAllCars();
}
