package com.otunba.medipro.repository;

import com.otunba.medipro.models.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findByUserId(String userId);
}
