package com.otunba.medipro.models;

import com.otunba.medipro.validators.NotEmptyOrNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Verification")
@AllArgsConstructor
@NoArgsConstructor
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private String token;
    private  boolean isVerified = false;
    private LocalDateTime expiryTime;
}
