package com.ilinskii.profileapp.repository;

import com.ilinskii.profileapp.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findFirstByOrderByCreatedDesc();

    Set<Profile> findByEmailIgnoreCase(String email);
}
