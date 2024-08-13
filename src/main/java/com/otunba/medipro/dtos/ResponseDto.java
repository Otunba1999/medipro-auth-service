package com.otunba.medipro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private String accessToken;
    private String refreshToken;
    private String message;
    private boolean flag;
    private boolean mFAEnabled;
}
