package com.nepath.carapp.dtos.output;

public class UserCarDto {
    private Long id;
    private String brand;
    private String model;
    private String engine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public UserCarDto(Long id, String brand, String model, String engine) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.engine = engine;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}
