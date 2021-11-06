package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.output.UserCarDto;
import com.nepath.carapp.models.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EngineMapper.class, ModelMapper.class, EngineMapper.class})
public interface CarMapper {
    CarMapper CAR_MAPPER = Mappers.getMapper(CarMapper.class);

//    @Mapping(target = "model", source = "model.name")
//    @Mapping(target = "brand", source = "model.brand.name")
//    @Mapping(target = "engine", source = "engine.name")
//    @Mapping(target = "userDto", source = "user")
//    CarDto carToCarDto(Car car);

    @Mapping(target = "model", source = "model.name")
    @Mapping(target = "brand", source = "model.brand.name")
    @Mapping(target = "engine", source = "engine.name")
    UserCarDto carToUserCarDto(Car car);
}
