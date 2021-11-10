package com.nepath.carapp.services;

import com.nepath.carapp.dtos.input.BrandCreateDto;
import com.nepath.carapp.dtos.input.ChangeCarOwnerDto;
import com.nepath.carapp.dtos.input.EngineCreateDto;
import com.nepath.carapp.dtos.input.ModelCreateDto;
import com.nepath.carapp.models.Brand;
import com.nepath.carapp.models.Engine;
import com.nepath.carapp.models.Model;

public interface AdminService {
    void createBrand(BrandCreateDto brandCreateDto);
    void createEngine(EngineCreateDto engineCreateDto);
    void createModel(ModelCreateDto modelCreateDto);


    void changeCarOwner(ChangeCarOwnerDto changeCarOwnerDto);
}
