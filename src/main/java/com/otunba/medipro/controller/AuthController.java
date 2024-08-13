package com.otunba.medipro.controller;

import com.otunba.medipro.dtos.*;
import com.otunba.medipro.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.UnknownHostException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid  @RequestBody UserDto userDto) {
        var response = authService.registerUser(userDto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.get("userId"))
                .toUri();
        var responseDto = ResponseDto.builder()
                .message(response.get("message"))
                .flag(true)
                .build();
        return ResponseEntity.created(location).body(responseDto);
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
        var response = authService.loginUser(loginDto);
        var responseDto = ResponseDto.builder()
                .message(response.get("message"))
                .accessToken(response.get("token"))
                .refreshToken(response.get("refreshToken"))
                .flag(true)
                .build();
        return ResponseEntity.ok(responseDto);
    }
    @PostMapping("/register/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(@RequestParam String refreshToken, @RequestParam String email) {
        var response = authService.refreshToken(email, refreshToken);
        var responseDto = ResponseDto.builder()
                .accessToken(response.get("token")).flag(true).build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/register/{token}")
    public  ResponseEntity<String> verifyEmail(@PathVariable("token") String token){
        var response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register/resend-verification/{email}")
    public ResponseEntity<Map<String, String>> resendVerification(@PathVariable("email") String email){
        var response = authService.resendVerificationEmail(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register/forgot-password/{email}")
    public ResponseEntity<Map<String, String>> forgotPassword(@PathVariable("email") String email){
        var response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/register/reset-password/{token}")
    public ModelAndView resetPasswordForm(@PathVariable("token") String token, Model model){
       ModelAndView modelAndView = new ModelAndView("reset-password");
       modelAndView.addObject("token", token);
       return modelAndView;
    }
    @PostMapping("/register/reset-password")
    public ModelAndView resetPassword(@RequestParam String token, @RequestParam String newPassword){
        ModelAndView modelAndView = new ModelAndView("password-success");
        var response = authService.resetPassword(token, newPassword);
        modelAndView.addObject("message", response.get("message"));
    return modelAndView;
    }
    @PostMapping("/register/2fa-authentication")
    public ResponseEntity<?> verfifyCode(@RequestBody  TwoFARequest request) throws UnknownHostException {
        var response = authService.verifyCode(request);
        return ResponseEntity.ok(response);
    }
}
