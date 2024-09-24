package com.otunba.medipro.services.impl;

import com.otunba.medipro.dtos.LoginDto;
import com.otunba.medipro.dtos.TwoFARequest;
import com.otunba.medipro.dtos.UserDto;
import com.otunba.medipro.enums.Role;
import com.otunba.medipro.exceptions.AuthException;
import com.otunba.medipro.models.User;
import com.otunba.medipro.models.Verification;
import com.otunba.medipro.repository.AttemptRepository;
import com.otunba.medipro.repository.RefreshTokenRepository;
import com.otunba.medipro.repository.UserRepository;
import com.otunba.medipro.repository.VerificationRepository;
import com.otunba.medipro.services.IEmailService;
import com.otunba.medipro.services.JwtTokenService;
import com.otunba.medipro.services.TwoFAAuthenticationService;
import com.otunba.medipro.utility.ModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.otunba.medipro.services.impl.AuthServiceImpl.getLink;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttemptRepository attemptRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ModelMapper<UserDto, User> userMapper;

    @Mock
    private TwoFAAuthenticationService tfaService;

    @Mock
    private JwtTokenService tokenService;

    @Mock
    private IEmailService emailService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setFirstname("Test");
        userDto.setMFAEnabled(false);

        user = new User();
        user.setId("someId");
        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPassword");
        user.setEnabled(false);
        user.setAccountNonLocked(true);
        user.setRole(Role.PATIENT);
        user.setLoginAttempts(new ArrayList<>());
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.mapFrom(userDto)).thenReturn(user);
        doReturn("encodedPassword").when(passwordEncoder).encode(anyString());
        when(tfaService.generateNewSecret()).thenReturn("secret");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(getLink(anyString())).thenReturn(anyString());

        Verification verification = new Verification();
        verification.setToken("verificationToken");
        when(verificationRepository.save(any(Verification.class))).thenReturn(verification);

        when(tfaService.generateQRCodeImageUri("secret")).thenReturn("qrCodeUri");

        // Act
        Map<String, String> response = authService.registerUser(userDto);

        // Assert
        assertEquals("Account created successfully. Proceed to verify you email address to enable this account.", response.get("message"));
        assertNotNull(response.get("userId"));
        assertEquals("qrCodeUri", response.get("secretUri"));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.registerUser(userDto));
        assertEquals("Email already exists, please try with another email", exception.getMessage());
    }

    @Test
    public void testLoginUser_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(tokenService.generateToken((Authentication) any())).thenReturn("token");
        when(tokenService.generateRefreshToken(user.getId())).thenReturn("refreshToken");

        // Act
        Map<String, String> response = authService.loginUser(new LoginDto(userDto.getEmail(), userDto.getPassword()));

        // Assert
        assertEquals("Successfully logged in", response.get("message"));
        assertEquals("token", response.get("token"));
        assertEquals("refreshToken", response.get("refreshToken"));
    }

    @Test
    public void testLoginUser_InvalidEmail() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.loginUser(new LoginDto(userDto.getEmail(), userDto.getPassword())));
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    public void testVerifyEmail_Success() {
        // Arrange
        Verification verification = new Verification();
        verification.setToken("validToken");
        verification.setUserId(user.getId());
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        when(verificationRepository.findByToken("validToken")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        String message = authService.verifyEmail("validToken");

        // Assert
        assertEquals("Account verified successfully", message);
        assertTrue(user.isEnabled());
        verify(verificationRepository).delete(verification);
    }

    @Test
    public void testVerifyEmail_InvalidToken() {
        // Arrange
        when(verificationRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.verifyEmail("invalidToken"));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    public void testForgotPassword_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(verificationRepository.save(any(Verification.class))).thenReturn(new Verification());

        // Act
        Map<String, String> response = authService.forgotPassword(userDto.getEmail());

        // Assert
        assertEquals("Reset password link sent to your email", response.get("message"));
    }

    @Test
    public void testResetPassword_Success() {
        // Arrange
        Verification verification = new Verification();
        verification.setToken("validToken");
        verification.setUserId(user.getId());
        when(verificationRepository.findByToken("validToken")).thenReturn(Optional.of(verification));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        Map<String, String> response = authService.resetPassword("validToken", "newPassword");

        // Assert
        assertEquals("Password updated successfully", response.get("message"));
        assertEquals("encodedNewPassword", user.getPassword());
        verify(verificationRepository).delete(verification);
    }

    @Test
    public void testResetPassword_InvalidToken() {
        // Arrange
        when(verificationRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.resetPassword("invalidToken", "newPassword"));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    public void testResendVerificationEmail_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(verificationRepository.findByUserId(user.getId())).thenReturn(Optional.of(new Verification()));
        when(verificationRepository.save(any(Verification.class))).thenReturn(new Verification());

        // Act
        Map<String, String> response = authService.resendVerificationEmail(userDto.getEmail());

        // Assert
        assertEquals("Verification email sent", response.get("message"));
    }

    @Test
    public void testResendVerificationEmail_InvalidEmail() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.resendVerificationEmail(userDto.getEmail()));
        assertEquals("Invalid email, provide email use for registration.", exception.getMessage());
    }

    @Test
    public void testVerifyCode_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(tfaService.isOtpNotValid(user.getSecret(), "validCode")).thenReturn(false);
        when(tokenService.generateToken(user)).thenReturn("token");

        // Act
        Map<String, String> response = authService.verifyCode(new TwoFARequest(userDto.getEmail(), "validCode"));

        // Assert
        assertEquals("Successfully logged in", response.get("Message"));
        assertEquals("token", response.get("token"));
    }

    @Test
    public void testVerifyCode_InvalidOTP() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(tfaService.isOtpNotValid(user.getSecret(), "invalidCode")).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.verifyCode(new TwoFARequest(userDto.getEmail(), "invalidCode")));
        assertEquals("OTP not valid", exception.getMessage());
    }

}