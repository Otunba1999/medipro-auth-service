package com.otunba.medipro.services;

import com.otunba.medipro.dtos.*;

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
    AuthResponse verifyJwtToken(String jwtToken);

}
