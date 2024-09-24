package com.otunba.medipro.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Table(name = "Refresh_Token")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    private String userId;
    private LocalDateTime expiryDate;
}
