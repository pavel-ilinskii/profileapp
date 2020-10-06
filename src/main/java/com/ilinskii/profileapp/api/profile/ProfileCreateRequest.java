package com.ilinskii.profileapp.api.profile;

import com.ilinskii.profileapp.model.Profile;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileCreateRequest {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = Profile.SIZE_NAME, message = "Name size should be between 1 and " + Profile.SIZE_NAME)
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email is incorrect")
    @Size(max = Profile.SIZE_EMAIL, message = "Email size should be less than " + Profile.SIZE_EMAIL)
    private String email;

    @Min(value = Profile.MIN_AGE, message = "Age should be greater than " + Profile.MIN_AGE)
    @Max(value = Profile.MAX_AGE, message = "Age should be less than " + Profile.MAX_AGE)
    @NotNull(message = "Age should be not empty")
    private Integer age;
}
