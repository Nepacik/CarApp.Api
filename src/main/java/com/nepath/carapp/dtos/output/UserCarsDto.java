package com.nepath.carapp.dtos.output;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class UserCarsDto {
    private Long id;
    private String nick;
    private String email;
    private List<CarDto> cars;
}
