package com.ilinskii.profileapp.api.profile;

import com.ilinskii.profileapp.exception.ValidationException;
import com.ilinskii.profileapp.model.Profile;
import com.ilinskii.profileapp.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private final ProfileConverter profileConverter;

    @PostMapping("/set")
    public ProfileIdResponse createProfile(@RequestBody @Valid ProfileCreateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        Profile profile = profileService.addProfile(profileConverter.convertToProfile(request));
        return profileConverter.convertToProfileId(profile);
    }

    @GetMapping("/last")
    public ProfileResponse getLastProfile() {
        Profile profile = profileService.getLastProfile();
        return profileConverter.convertToProfileResponse(profile);
    }

    @GetMapping
    public List<ProfileResponse> getAllProfiles() {
        return profileService.getAllProfiles().stream()
                .map(profileConverter::convertToProfileResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfile(@PathVariable Long id) {
        Profile profile = profileService.getProfile(id);
        return profileConverter.convertToProfileResponse(profile);
    }

    @PostMapping("/get")
    public ProfileResponse getProfileByEmail(@RequestBody @Valid ProfileFilterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        Profile profile = profileService.getByEmail(request.getEmail());
        return profileConverter.convertToProfileResponse(profile);
    }
}
