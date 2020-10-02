package com.ilinskii.profileapp.repository;

import com.ilinskii.profileapp.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Optional<ErrorLog> findFirstByOrderByCreatedDesc();
}
