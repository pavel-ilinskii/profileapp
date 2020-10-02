package com.ilinskii.profileapp.service;

import com.ilinskii.profileapp.exception.ExecutionConflictException;
import com.ilinskii.profileapp.exception.NotFoundObjectException;
import com.ilinskii.profileapp.model.Profile;
import com.ilinskii.profileapp.repository.ProfileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public Profile addProfile(@NonNull Profile profile) {
        if (null != profile.getId()) {
            throw new ExecutionConflictException("Profile is already exist");
        }

        profile.setCreated(ZonedDateTime.now());

        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Profile getProfile(@NonNull Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundObjectException("Profile not found. id = " + id));
    }

    @Transactional(readOnly = true)
    public Profile getLastProfile() {
        return profileRepository.findFirstByOrderByCreatedDesc()
                .orElseThrow(() -> new NotFoundObjectException("No profiles found"));
    }

    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profile getByEmail(@NonNull String email) {
        Set<Profile> profiles = profileRepository.findByEmailIgnoreCase(email);

        if (1 < profiles.size()) {
            throw new IllegalStateException("Non unique emails found");
        }

        return profiles.stream().findFirst()
                .orElseThrow(() -> new NotFoundObjectException("Profile not found. email = " + email));
    }
}
