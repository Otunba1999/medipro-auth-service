package com.otunba.medipro.services;

import com.otunba.medipro.dtos.AuthResponse;
import com.otunba.medipro.exceptions.AuthException;
import com.otunba.medipro.models.RefreshToken;
import com.otunba.medipro.models.User;
import com.otunba.medipro.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateToken(Authentication authentication) {
        var principal = (User)authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expiresAt = Instant.now().plusSeconds(60 * 60 * 12);
        List<String> scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("authorities", scopes)
                .claim("userId", principal.getId())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = Instant.now().plusSeconds(60 * 60 * 24);
        List<String> scopes = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getEmail())
                .claim("authorities", scopes)
                .claim("userId", user.getId())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(String userId){
        var refreshToken = RefreshToken.builder()
                .userId(userId)
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        var savedRefreshToken = refreshTokenRepository.save(refreshToken);
        return  savedRefreshToken.getRefreshToken();
    }

    public Instant getExpirtaionTime(String token){
        Jwt decodedToken = getDecodedToken(token);
        return Instant.from(Objects.requireNonNull(decodedToken.getExpiresAt()));
    }

    public Object getAuthorities(String token) {
        Jwt decodedToken = getDecodedToken(token);
        return decodedToken.getClaims().get("authorities");
    }
    public String getUserEmail(String token) {
        Jwt decodedToken = getDecodedToken(token);
        return decodedToken.getClaimAsString("sub");
    }
    public AuthResponse decodeJwt(String jwt){
        Jwt decodedToken = getDecodedToken(jwt);
        var userId = decodedToken.getClaims().get("userId");
        var authorities = decodedToken.getClaimAsString("authorities");
        return new AuthResponse(authorities, userId.toString());
    }

    public boolean isTokenValid(String token) throws IOException {
        Jwt decodedToken = getDecodedToken(token);
        return Objects.requireNonNull(decodedToken.getExpiresAt()).isAfter(Instant.now());
    }
    private Jwt getDecodedToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new AuthException("Invalid Jwt token, try with a valid token: token service");
        }
    }

}



