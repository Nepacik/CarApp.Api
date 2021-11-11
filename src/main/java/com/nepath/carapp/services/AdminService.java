package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.BrandCreateDto;
import com.nepath.carapp.dtos.input.ChangeCarOwnerDto;
import com.nepath.carapp.dtos.input.EngineCreateDto;
import com.nepath.carapp.dtos.input.ModelCreateDto;

public interface AdminService {
    void createBrand(BrandCreateDto brandCreateDto);

    void createEngine(EngineCreateDto engineCreateDto);

    void createModel(ModelCreateDto modelCreateDto);


    void changeCarOwner(ChangeCarOwnerDto changeCarOwnerDto);
}
