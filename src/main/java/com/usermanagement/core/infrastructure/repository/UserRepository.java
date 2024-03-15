package com.usermanagement.core.infrastructure.repository;

import com.usermanagement.core.infrastructure.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Searches for a User entity by its email address.
     *
     * @param email the email address to search for.
     * @return an Optional containing the User entity if found, or an empty Optional if no user is found with the specified email.
     */
    Optional<User> findByEmail(String email);
}