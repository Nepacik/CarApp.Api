package com.nepath.carapp.mappers;

import com.nepath.carapp.dtos.input.BrandCreateDto;
import com.nepath.carapp.models.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandMapper BRAND_MAPPER = Mappers.getMapper(BrandMapper.class);

    Brand brandDtoToBrand(BrandCreateDto brandCreateDto);
}
