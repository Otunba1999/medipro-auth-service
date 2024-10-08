package com.otunba.medipro.repository;

import com.otunba.medipro.models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByToken(String token);

    Optional<Verification> findByUserId(String id);

}
