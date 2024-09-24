package com.otunba.medipro.dtos;

import com.otunba.medipro.validators.NotEmptyOrNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwoFARequest {
    @NotEmptyOrNull
    private String email;
    @NotEmptyOrNull
    private String code;
}