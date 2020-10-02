package com.ilinskii.profileapp.api.errorlog;

import com.ilinskii.profileapp.model.ErrorLog;
import com.ilinskii.profileapp.service.ErrorLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErrorLogController {

    private final ErrorLogService errorLogService;

    private final ErrorLogConverter errorLogConverter;

    @GetMapping("/last")
    public ErrorLogResponse getLastErrorLog() {
        ErrorLog errorLog = errorLogService.getLastErrorLog();
        return errorLogConverter.convertToErrorLogResponse(errorLog);
    }
}
