package com.otunba.medipro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.otunba.medipro.enums.Role;
import com.otunba.medipro.validators.NotEmptyOrNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    @NotEmptyOrNull
    @Email(message = "email must be a valid email")
    private String email;
    private String firstName;
    private String lastName;
    @NotEmptyOrNull
    private String password;
    @NotEmptyOrNull
    private String phoneNumber;
    @NotNull
    private  boolean mFAEnabled = false;
    private Role role;
}
