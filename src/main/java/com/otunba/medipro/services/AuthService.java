package com.otunba.medipro.services;

import com.otunba.medipro.dtos.LoginDto;
import com.otunba.medipro.dtos.RegistrationDto;
import com.otunba.medipro.dtos.TwoFARequest;
import com.otunba.medipro.dtos.UserDto;

import java.net.UnknownHostException;
import java.util.Map;

public interface AuthService {
    Map<String, String> registerUser(UserDto userDto);
    Map<String, String> loginUser(LoginDto loginDto);
    String verifyEmail(String token);
    Map<String, String> resendVerificationEmail(String email);
    Map<String, String> forgotPassword(String email);
    Map<String, String> resetPassword(String token, String newPassword);
    Map<String, String> refreshToken(String email, String refreshToken);
    Map<String, String> verifyCode(TwoFARequest request) throws UnknownHostException;

}
