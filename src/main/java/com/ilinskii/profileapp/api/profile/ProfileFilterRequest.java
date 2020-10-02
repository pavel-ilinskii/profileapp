package com.ilinskii.profileapp.api.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ProfileFilterRequest {

    @NotEmpty(message = "Email should not be empty")
    private String email;
}
