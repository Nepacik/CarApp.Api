package com.nepath.carapp.services.implementation;

import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.models.Brand;
import com.nepath.carapp.models.Engine;
import com.nepath.carapp.models.Model;
import com.nepath.carapp.repositories.*;
import com.nepath.carapp.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final EngineRepository engineRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Override
    public void createBrand(Brand brand) {
        try {
            brandRepository.save(brand);
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Brand already exists");
        } catch (Exception e) {
            throw new ApiRequestException.ServerErrorException();
        }
    }

    @Override
    public void createEngine(Engine engine) {
        try {
            engineRepository.save(engine);
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Engine already exists");
        } catch (Exception e) {
            throw new ApiRequestException.ServerErrorException();
        }
    }

    @Override
    public void createModel(Model model) {
        try {
            if(!brandRepository.existsById(model.getBrand().getId())) {
                throw new ApiRequestException.NotFoundErrorException("Brand does not exists");
            }
            modelRepository.save(model);
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Model already exists");
        } catch (Exception e) {
            throw new ApiRequestException.ServerErrorException();
        }
    }

    @Override
    public void changeCarOwner(Long carId, Long userId) {
        try {
            if (!carRepository.existsById(carId)) {
                throw new ApiRequestException.NotFoundErrorException("Car does not exists");
            }
            if (!userRepository.existsById(userId)) {
                throw new ApiRequestException.NotFoundErrorException("User does not exists");
            }
            carRepository.changeCarOwner( carId, userId);
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Model already exists");
        } catch (Exception e) {
            throw new ApiRequestException.ServerErrorException();
        }
    }

}
