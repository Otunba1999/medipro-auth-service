package com.otunba.medipro.models;

import com.otunba.medipro.enums.Gender;
import com.otunba.medipro.enums.Specialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Profile {
    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    private int licenceNumber;
    @OneToOne
    private User user;
}
