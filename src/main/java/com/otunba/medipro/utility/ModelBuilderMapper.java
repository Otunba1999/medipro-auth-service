package com.otunba.medipro.utility;

import com.otunba.medipro.dtos.*;
import org.springframework.stereotype.Component;

@Component
public class ModelBuilderMapper implements ModelMapper<RegistrationDto, ModelBuilder>{
    @Override
    public RegistrationDto mapTo(ModelBuilder modelBuilder) {
        return null;
    }

    @Override
    public ModelBuilder mapFrom(RegistrationDto registrationDto) {
        UserDto user = UserDto.builder()
                .firstname(registrationDto.getFirstname())
                .lastname(registrationDto.getLastname())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword())
                .build();
        return ModelBuilder.builder()
                .user(user)
                .build();
    }
}
