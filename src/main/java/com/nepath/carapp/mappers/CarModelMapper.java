package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.input.ModelCreateDto;
import com.nepath.carapp.models.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarModelMapper {
    CarModelMapper MODEL_MAPPER = Mappers.getMapper(CarModelMapper.class);

    @Mapping(target = "brand.id", source = "brandId")
    Model modelDtoToModel(ModelCreateDto modelCreateDto);
}
