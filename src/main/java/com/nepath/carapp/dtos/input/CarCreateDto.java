package com.nepath.carapp.dtos.input;

import lombok.Data;

@Data
public class CarCreateDto {
    private Long modelId;
    private Long engineID;
    private Long userId;
}
