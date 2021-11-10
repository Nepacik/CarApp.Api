package com.nepath.carapp.dtos.input;

import lombok.Data;

@Data
public class ChangeCarOwnerDto {
    private Long carId;
    private Long newOwnerId;
}
