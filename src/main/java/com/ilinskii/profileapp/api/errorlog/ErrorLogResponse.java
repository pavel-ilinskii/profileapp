package com.ilinskii.profileapp.api.errorlog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorLogResponse {

    private String msg;

    private Long created;
}
