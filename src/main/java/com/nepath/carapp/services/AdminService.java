package com.nepath.carapp.services;

import com.nepath.carapp.models.Brand;
import com.nepath.carapp.models.Engine;
import com.nepath.carapp.models.Model;

public interface AdminService {
    void createBrand(Brand brand);
    void createEngine(Engine engine);
    void createModel(Model model);


    void changeCarOwner(Long carId, Long userId);
}
