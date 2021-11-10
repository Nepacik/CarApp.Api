package com.nepath.carapp.dtos.input;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RefreshTokenDto {
    @NotEmpty
    @NotNull
    private String refreshToken;
}
