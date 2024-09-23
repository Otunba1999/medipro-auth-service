package com.otunba.medipro.controller;

import com.otunba.medipro.dtos.*;
import com.otunba.medipro.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth management", description = "Operations related  to authentication")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register a new user to the system")
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
    @Operation(summary = "Login a user / Authenticate a user")
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
    @Operation(summary = "issue a new access token to the user if the refresh token is still valid.")
    public ResponseEntity<ResponseDto> refreshToken(@RequestParam String refreshToken, @RequestParam String email) {
        var response = authService.refreshToken(email, refreshToken);
        var responseDto = ResponseDto.builder()
                .accessToken(response.get("token")).flag(true).build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/register/{token}")
    @Operation(summary = "verify the email of the newly created user")
    public  ResponseEntity<String> verifyEmail(@PathVariable("token") String token){
        var response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register/resend-verification/{email}")
    @Operation(summary = "resend verification again if the previous one is expired")
    public ResponseEntity<Map<String, String>> resendVerification(@PathVariable("email") String email){
        var response = authService.resendVerificationEmail(email);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register/forgot-password/{email}")
    @Operation(summary = "send a link to the user account to reset password")
    public ResponseEntity<Map<String, String>> forgotPassword(@PathVariable("email") String email){
        var response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/register/reset-password/{token}")
    @Operation(summary = "provide interface for user to reset their password")
    public ModelAndView resetPasswordForm(@PathVariable("token") String token, Model model){
       ModelAndView modelAndView = new ModelAndView("reset-password");
       modelAndView.addObject("token", token);
       return modelAndView;
    }
    @PostMapping("/register/reset-password")
    @Operation(summary = "reset the user password to the newly created password.")
    public ModelAndView resetPassword(@RequestParam String token, @RequestParam String newPassword){
        ModelAndView modelAndView = new ModelAndView("password-success");
        var response = authService.resetPassword(token, newPassword);
        modelAndView.addObject("message", response.get("message"));
    return modelAndView;
    }
    @PostMapping("/register/2fa-authentication")
    @Operation(summary = "enable 2factor authentication for user (still under development)")
    public ResponseEntity<?> verfifyCode(@RequestBody  TwoFARequest request) throws UnknownHostException {
        var response = authService.verifyCode(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/register/auth")
    @Operation(summary = "verify and authenticate jwt token from other micro-service")
    public ResponseEntity<?> verifyJwt(@RequestParam String token){
        var response = authService.verifyJwtToken(token);
        return ResponseEntity.ok(response);
    }
}
