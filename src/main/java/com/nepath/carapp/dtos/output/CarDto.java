package com.nepath.carapp.dtos.output;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarDto implements Serializable {
    private Long id;
    private String brand;
    private String model;
    private String engine;
    private Long userId;
}
