package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.input.CarCreateDto;
import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.dtos.output.UserDto;
import com.nepath.carapp.models.Car;
import com.nepath.carapp.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarMapper CAR_MAPPER = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "model", source = "model.name")
    @Mapping(target = "brand", source = "model.brand.name")
    @Mapping(target = "engine", source = "engine.name")
    @Mapping(target = "userId", source = "user.id")
    CarDto carToCarDto(Car cars);

    List<CarDto> carToCarDto(List<Car> cars);

    @Mapping(target = "model.id", source = "modelId")
    @Mapping(target = "engine.id", source = "engineID")
    Car carCreateDtoToCar(CarCreateDto carCreateDto);
}
