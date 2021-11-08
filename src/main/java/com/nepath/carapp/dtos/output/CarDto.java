package com.nepath.carapp.dtos.output;

import lombok.Data;

@Data
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private String engine;
    private Long userId;
}
