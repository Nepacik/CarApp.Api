package com.nepath.carapp.dtos.input;

import com.nepath.carapp.dtos.output.CarDto;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserCreateDto {

    @NotEmpty(message = "Cant be mepty")
    @Size(min = 5, max = 30, message = "Nick must not be null")
    private String nick;

    @NotEmpty(message = "Cant be mepty")
    @Email(message = "message")
    private String email;

    @NotEmpty(message = "Cant be mepty")
    @Size(min = 8, max = 30)
    private String password;
}
