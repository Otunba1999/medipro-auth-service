package com.otunba.medipro.services.impl;

import com.otunba.medipro.dtos.*;
import com.otunba.medipro.enums.Role;
import com.otunba.medipro.exceptions.AuthException;
import com.otunba.medipro.models.LoginAttempt;
import com.otunba.medipro.models.User;
import com.otunba.medipro.models.Verification;
import com.otunba.medipro.repository.AttemptRepository;
import com.otunba.medipro.repository.RefreshTokenRepository;
import com.otunba.medipro.repository.UserRepository;
import com.otunba.medipro.repository.VerificationRepository;
import com.otunba.medipro.services.AuthService;
import com.otunba.medipro.services.IEmailService;
import com.otunba.medipro.services.JwtTokenService;
import com.otunba.medipro.services.TwoFAAuthenticationService;
import com.otunba.medipro.utility.MailMessage;
import com.otunba.medipro.utility.ModelMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;
    private final VerificationRepository verificationRepository;
    private  final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper<UserDto, User> userMapper;
    private final TwoFAAuthenticationService tfaService;
    private final JwtTokenService tokenService;
    private final IEmailService emailService;

    @Override
    @Transactional
    public Map<String, String> registerUser(UserDto userDto) {
        var existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new AuthException("Email already exists, please choose another one");
        }
        var user = userMapper.mapFrom(userDto);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.PATIENT);
        // check if 2FA is enabled
        if(!user.isMFAEnabled()){
            user.setSecret(tfaService.generateNewSecret());
        }

        var savdUser = userRepository.save(user);
        var token = getVerificationToken(user);
        var link = getLink(token);
        log.info("generated link: {}", link);
        var message = MailMessage.getVerificationMessage(user.getFirstName(), link);

        MailRequest mailRequest = new MailRequest(user.getEmail(), "Verification email", message);
//        emailService.sendVerificationEmail(mailRequest);

        return Map.of("message", "Account created successfully. Proceed to verify you email address to enable this account.",
                "userId", savdUser.getId(),
                "secretUri", tfaService.generateQRCodeImageUri(user.getSecret()));
    }

    @Override
    public Map<String, String> loginUser(LoginDto loginDto) {
        var user = doesUserExist(loginDto.getEmail());
        if (user.isEmpty()) throw new AuthException("Invalid email");
        if (!user.get().isAccountNonLocked())
            throw new AuthException("This account is locked due to too many invalid login attempt. An email will be sent shortly on how to unlock your account.");
        var attempts = user.get().getLoginAttempts();
        var isValidLoginAttempt = isValidLoginAttempt(attempts);
        if (!isValidLoginAttempt) handleInvalidLoginAttempt(user.get());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            if(user.get().isMFAEnabled()){
                return Map.of("2FAEnabled", "true");
            }
            var token = tokenService.generateToken(authentication);
            var refreshToken = tokenService.generateRefreshToken(user.get().getId());
            log.info("token expiration = {}", tokenService.getExpirtaionTime(token));
            log.info("user authorities = {}", tokenService.getAuthorities(token));
//            log.info("is token valid = {}", tokenService.isTokenValid(token));
            handleNumberOfTrials(user.get(), true);
            return Map.of("message", "Successfully logged in", "token", token, "refreshToken", refreshToken);
        } catch (AuthenticationException e) {
            handleNumberOfTrials(user.get(), false);
            throw new AuthException("Invalid email or password " + e.getMessage());
        }

    }

    @Override
    public Map<String, String> refreshToken(String email, String tok) {
        var user = doesUserExist(email);
        if (user.isEmpty()) throw new AuthException("Invalid email");
        var savedToken = refreshTokenRepository.findByRefreshToken(tok);
        if(savedToken.isEmpty() || !user.get().getId().equals(savedToken.get().getUserId())) throw new AuthException("Invalid token");
        if(savedToken.get().getExpiryDate().isBefore(LocalDateTime.now())) throw new AuthException("Expired token");
        var token = tokenService.generateToken(user.get());
        return Map.of( "token", token);
    }

    @Override
    public String verifyEmail(String token) {
        var verification = verificationRepository.findByToken(token);
        if (verification.isEmpty()) throw new AuthException("Invalid token");
        if (verification.get().getExpiryTime().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(verification.get());
            throw new AuthException("verification expired");
        }
        verification.get().setVerified(true);
        var user = userRepository.findById(verification.get().getUserId());
        if (user.isEmpty()) throw new AuthException("Invalid user");
        user.get().setEnabled(true);
        userRepository.save(user.get());
        verificationRepository.delete(verification.get());
        return "Account verified successfully";
    }

    @Override
    public Map<String, String> resendVerificationEmail(String email) {
        var user = doesUserExist(email);
        if (user.isEmpty()) throw new AuthException("Invalid email, provide email use for registration.");
        var existingVerification = verificationRepository.findByUserId(user.get().getId());
        if (existingVerification.isEmpty()) throw new AuthException("Invalid user");
        verificationRepository.delete(existingVerification.get());
        var token = getVerificationToken(user.get());
        var link = "http://localhost:8181/auth/register/" + token;
        var message = MailMessage.getVerificationMessage(user.get().getFirstName(), link);
        var request = new MailRequest(email, "Verification email", message);
        emailService.sendVerificationEmail(request);
        return Map.of("message", "Verification email sent");
    }

    @Override
    public Map<String, String> forgotPassword(String email) {
        var user = doesUserExist(email);
        if (user.isEmpty()) throw new AuthException("Invalid email, provide email use for registration.");
        var token = getVerificationToken(user.get());
        var link =  "http://localhost:8181/auth/register/reset-password/" + token;
        log.info("generated link for reset password: {}", link);
        var message = MailMessage.getPasswordResetMessage(user.get().getFirstName(), link);
        var request = new MailRequest(email, "Password reset email", message);
        emailService.sendVerificationEmail(request);
        return Map.of("message", "Reset password link sent to your email");
    }

    @Override
    public Map<String, String> resetPassword(String token, String newPassword) {
        var verification = verificationRepository.findByToken(token);
        if (verification.isEmpty()) throw new AuthException("Invalid token");
        var user = userRepository.findById(verification.get().getUserId());
        if (user.isEmpty()) throw new AuthException("Invalid user");
        user.get().setPassword(passwordEncoder.encode(newPassword));
        log.info("Password reset to {}", newPassword);
        userRepository.save(user.get());
        verificationRepository.delete(verification.get());
        return Map.of("message", "Password updated successfully");
    }

    @Override
    public Map<String, String> verifyCode(TwoFARequest request) {
        var user = doesUserExist(request.getEmail());
        if (user.isEmpty()) throw new AuthException("Invalid email.");
        var isVerificationValid = tfaService.isOtpNotValid(user.get().getSecret(), request.getCode());
        if(isVerificationValid) throw new AuthException("OTP not valid");
        var token = tokenService.generateToken(user.get());
        return Map.of("Message", "Successfully logged in", "token", token);
    }

    @Override
    public AuthResponse verifyJwtToken(String jwtToken) {
        return tokenService.decodeJwt(jwtToken);
    }

    public static String getLink(String token) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{token}")
                .buildAndExpand(token).toUri().toString();
    }


    private String getVerificationToken(User user) {
        var verification = new Verification();
        verification.setUserId(user.getId());
        verification.setToken(UUID.randomUUID().toString());
        verification.setVerified(false);
        verification.setExpiryTime(LocalDateTime.now().plus(Duration.ofMinutes(1)));
        return verificationRepository.save(verification).getToken();

    }


    private void handleInvalidLoginAttempt(User user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
        throw new AuthException("Too many attempts, account is disabled");
    }

    private boolean isValidLoginAttempt(List<LoginAttempt> attempts) {
        var lastFiveAttempts = getLastFiveAttempts(attempts);
        if (lastFiveAttempts.size() == 5) {
            for (var attempt : lastFiveAttempts) {
                if (attempt.isSuccess()) {
                    return true;
                }
            }
            LocalDateTime dateA = lastFiveAttempts.get(0).getDate();
            LocalDateTime dateB = lastFiveAttempts.get(lastFiveAttempts.size() - 1).getDate();
            return isWithinRange(dateA, dateB);
        }
        return true;
    }

    private boolean isWithinRange(LocalDateTime a, LocalDateTime b) {
        Duration duration = Duration.between(a, b);
        long seconds = duration.getSeconds();
        return Math.abs(seconds) >= 5;
    }

    private void handleNumberOfTrials(User user, boolean status) {
        var attempt = LoginAttempt.builder()
                .success(status)
                .date(LocalDateTime.now())
                .user(user)
                .build();
        user.getLoginAttempts().add(attempt);
        attemptRepository.save(attempt);
    }

    private Optional<User> doesUserExist(String email) {
        return userRepository.findByEmail(email);
    }

    private List<LoginAttempt> getLastFiveAttempts(List<LoginAttempt> attempts) {
        int size = attempts.size();
        return attempts.subList(Math.max(size - 5, 0), size);
    }
}
