package com.nepath.carapp.services.implementation;

import com.nepath.carapp.dtos.input.BrandCreateDto;
import com.nepath.carapp.dtos.input.ChangeCarOwnerDto;
import com.nepath.carapp.dtos.input.EngineCreateDto;
import com.nepath.carapp.dtos.input.ModelCreateDto;
import com.nepath.carapp.exceptions.ApiRequestException;
import com.nepath.carapp.mappers.BrandMapper;
import com.nepath.carapp.mappers.CarModelMapper;
import com.nepath.carapp.mappers.EngineMapper;
import com.nepath.carapp.repositories.*;
import com.nepath.carapp.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final EngineRepository engineRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BrandMapper brandMapper;
    private final EngineMapper engineMapper;
    private final CarModelMapper carModelMapper;

    @Override
    public void createBrand(BrandCreateDto brandCreateDto) {
        try {
            brandRepository.save(brandMapper.brandDtoToBrand(brandCreateDto));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Brand already exists");
        }
    }

    @Override
    public void createEngine(EngineCreateDto engineCreateDto) {
        try {
            engineRepository.save(engineMapper.engineDtoToEngine(engineCreateDto));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Engine already exists");
        }
    }

    @Override
    public void createModel(ModelCreateDto modelCreateDto) {
        try {
            if(!brandRepository.existsById(modelCreateDto.getBrandId())) {
                throw new ApiRequestException.NotFoundErrorException("Brand does not exists");
            }
            modelRepository.save(carModelMapper.modelDtoToModel(modelCreateDto));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Model already exists");
        }
    }

    @Override
    @Transactional
    public void changeCarOwner(ChangeCarOwnerDto changeCarOwnerDto) {
        try {
            if (!carRepository.existsById(changeCarOwnerDto.getCarId())) {
                throw new ApiRequestException.NotFoundErrorException("Car does not exists");
            }
            if (!userRepository.existsById(changeCarOwnerDto.getNewOwnerId())) {
                throw new ApiRequestException.NotFoundErrorException("User does not exists");
            }
            if(carRepository.existsCarWithOwnerId(changeCarOwnerDto.getCarId(), changeCarOwnerDto.getNewOwnerId())) {
                throw new ApiRequestException.NotFoundErrorException("Car already belongs to this owner");
            }
            carRepository.changeCarOwner(changeCarOwnerDto.getCarId(), changeCarOwnerDto.getNewOwnerId());
        } catch (DataIntegrityViolationException exception) {
            throw new ApiRequestException.ConflictException("Model already exists");
        }
    }

}
