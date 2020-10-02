package com.ilinskii.profileapp.api.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {

    private Long id;

    private String email;

    private String name;

    private Integer age;

    private Long created;
}
