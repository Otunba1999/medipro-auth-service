package com.otunba.medipro.services.impl;

import com.otunba.medipro.dtos.UserDto;
import com.otunba.medipro.models.User;
import com.otunba.medipro.repository.UserRepository;
import com.otunba.medipro.services.UserService;
import com.otunba.medipro.utility.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper<UserDto, User> userModelMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nit found"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userModelMapper::mapTo).toList();
    }
}
