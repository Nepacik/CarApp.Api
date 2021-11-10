package com.nepath.carapp.dtos.input;

import com.nepath.carapp.dtos.output.CarDto;
import com.nepath.carapp.exceptions.ApiException;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserCreateDto {

    @NotNull
    @NotEmpty(message = "Cant be empty")
    @Size(min = 5, max = 30, message = "Nick size error")
    private String nick;

    @NotEmpty(message = "Cant be empty")
    @Email(message = "Email is not correct")
    private String email;

    @NotEmpty(message = "Cant be empty")
    @Size(min = 8, max = 30, message = "Nick size error")
    private String password;
}
