package com.nepath.carapp.dtos.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserCarsDto implements Serializable {
    private Long id;
    private String nick;
    private String email;
    private List<CarDto> cars;
}
