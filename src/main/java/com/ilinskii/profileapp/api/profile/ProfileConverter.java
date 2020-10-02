package com.ilinskii.profileapp.api.profile;

import com.ilinskii.profileapp.model.Profile;
import lombok.NonNull;
import org.springframework.stereotype.Component;


@Component
public class ProfileConverter {

    public Profile convertToProfile(@NonNull ProfileCreateRequest request) {
        Profile profile = new Profile();

        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        profile.setAge(request.getAge());

        return profile;
    }

    public ProfileIdResponse convertToProfileId(@NonNull Profile profile) {
        ProfileIdResponse response = new ProfileIdResponse();

        response.setIdUser(profile.getId());

        return response;
    }

    public ProfileResponse convertToProfileResponse(@NonNull Profile profile) {
        ProfileResponse profileResponse = new ProfileResponse();

        profileResponse.setId(profile.getId());
        profileResponse.setEmail(profile.getEmail());
        profileResponse.setAge(profile.getAge());
        profileResponse.setName(profile.getName());
        profileResponse.setCreated(null == profile.getCreated() ? null : profile.getCreated().toInstant().toEpochMilli());

        return profileResponse;
    }
}
