package com.ilinskii.profileapp.service;

import com.ilinskii.profileapp.exception.NotFoundObjectException;
import com.ilinskii.profileapp.model.ErrorLog;
import com.ilinskii.profileapp.repository.ErrorLogRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;

    @Transactional
    public ErrorLog logError(@NonNull String msg) {
        ErrorLog errorLog = new ErrorLog();

        errorLog.setMsg(msg);
        errorLog.setCreated(ZonedDateTime.now());

        return errorLogRepository.save(errorLog);
    }

    @Transactional(readOnly = true)
    public ErrorLog getLastErrorLog() {
        return errorLogRepository.findFirstByOrderByCreatedDesc()
                .orElseThrow(() -> new NotFoundObjectException("No error logs found"));
    }
}
