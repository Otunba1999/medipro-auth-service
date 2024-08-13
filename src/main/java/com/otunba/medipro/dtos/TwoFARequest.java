package com.otunba.medipro.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwoFARequest {
    private String email;
    private String code;
}