package com.otunba.medipro.utility;

import com.otunba.medipro.dtos.UserDto;
import com.otunba.medipro.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements ModelMapper<UserDto, User>{

    @Override
    public UserDto mapTo(User user) {
        return UserDto.builder()
                .role(user.getRole())
                .lastname(user.getLastName())
                .firstname(user.getFirstName())
                .email(user.getEmail())
                .mFAEnabled(user.isMFAEnabled())
                .id(user.getId())
                .build();
    }

    @Override
    public User mapFrom(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstname())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .mFAEnabled(userDto.isMFAEnabled())
                .build();
    }
}
