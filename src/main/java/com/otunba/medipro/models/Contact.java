package com.otunba.medipro.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contact {
    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String phone;
    private String street;
    private String city;
    @Column(nullable = false)
    private String state;
    private String postalCode;
    @OneToOne
    private User user;
}
