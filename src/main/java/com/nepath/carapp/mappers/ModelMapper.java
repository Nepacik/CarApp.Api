package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.input.ModelCreateDto;
import com.nepath.carapp.models.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModelMapper {
    ModelMapper MODEL_MAPPER = Mappers.getMapper(ModelMapper.class);

    @Mapping(target = "brand.id", source = "brandId")
    Model modelDtoToModel(ModelCreateDto modelCreateDto);
}
