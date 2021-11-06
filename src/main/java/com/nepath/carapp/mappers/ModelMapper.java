package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.output.ModelDto;
import com.nepath.carapp.models.Model;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModelMapper {
    ModelMapper MODEL_MAPPER = Mappers.getMapper(ModelMapper.class);

    ModelDto modelToModelDto(Model model);
}
