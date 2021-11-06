package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.output.EngineDto;
import com.nepath.carapp.models.Engine;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EngineMapper {
    EngineMapper ENGINE_MAPPER = Mappers.getMapper(EngineMapper.class);

    EngineDto engineToEngineDto(Engine model);
}
