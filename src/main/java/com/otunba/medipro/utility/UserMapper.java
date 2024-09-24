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
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .mFAEnabled(user.isMFAEnabled())
                .id(user.getId())
                .build();
    }

    @Override
    public User mapFrom(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .mFAEnabled(userDto.isMFAEnabled())
                .build();
    }
}
