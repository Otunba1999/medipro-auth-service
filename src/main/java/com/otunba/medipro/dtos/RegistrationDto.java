package com.otunba.medipro.dtos;

import com.otunba.medipro.enums.Gender;
import com.otunba.medipro.enums.Specialization;
import com.otunba.medipro.validators.NotEmptyOrNull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegistrationDto {
    @NotEmptyOrNull
    @Email(message = "email must be a valid email")
    private String email;
    @NotEmptyOrNull
    private String password;
    @NotEmptyOrNull
    private String firstname;
    @NotEmptyOrNull
    private String lastname;
    private boolean  is2FAEnabled;
    @NotEmptyOrNull
    private String phone;

}
