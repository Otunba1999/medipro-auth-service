package com.otunba.medipro.utility;

import com.otunba.medipro.dtos.ProfileDto;
import com.otunba.medipro.models.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements ModelMapper<ProfileDto, Profile>{
    @Override
    public ProfileDto mapTo(Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .dob(profile.getDob())
                .licenceNumber(profile.getLicenceNumber())
                .specialization(profile.getSpecialization())
                .gender(profile.getGender())
                .build();
    }

    @Override
    public Profile mapFrom(ProfileDto profileDto) {
        return Profile.builder()
                .dob(profileDto.getDob())
                .licenceNumber(profileDto.getLicenceNumber())
                .specialization(profileDto.getSpecialization())
                .gender(profileDto.getGender())
                .build();
    }
}
