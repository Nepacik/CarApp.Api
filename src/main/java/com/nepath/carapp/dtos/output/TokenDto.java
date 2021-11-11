package com.nepath.carapp.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TokenDto implements Serializable {
    private String accessToken;
    private String refreshToken;
}
