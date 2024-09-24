package com.otunba.medipro.dtos;

import com.otunba.medipro.validators.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    @NotEmptyOrNull
    private String email;
    @NotEmptyOrNull
    private String password;
}
