package com.ilinskii.profileapp.api.errorlog;

import com.ilinskii.profileapp.model.ErrorLog;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogConverter {

    public ErrorLogResponse convertToErrorLogResponse(@NonNull ErrorLog errorLog) {
        ErrorLogResponse errorLogResponse = new ErrorLogResponse();

        errorLogResponse.setMsg(errorLog.getMsg());
        errorLogResponse.setCreated(null == errorLog.getCreated() ? null : errorLog.getCreated().toInstant().toEpochMilli());

        return errorLogResponse;
    }
}
