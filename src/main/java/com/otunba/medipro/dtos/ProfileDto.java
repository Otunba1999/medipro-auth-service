package com.otunba.medipro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.otunba.medipro.enums.Gender;
import com.otunba.medipro.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private String id;
    private Gender gender;
    private LocalDate dob;
    private Specialization specialization;
    private int licenceNumber;
}
