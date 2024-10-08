package com.otunba.medipro;

import com.otunba.medipro.dtos.*;
import com.otunba.medipro.enums.Role;
import com.otunba.medipro.models.User;

public  class TestModels {

    public static UserDto getUserDto1() {
        return UserDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("test@gmail.com")
                .password("password")
                .mFAEnabled(false)
                .build();
    }
    public static User getUser1() {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@gmail.com")
                .password("password")
                .role(Role.PATIENT)
                .id("some-id")
                .build();
    }


}
