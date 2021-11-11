package com.nepath.carapp.dtos.output;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String nick;
    private String email;
}