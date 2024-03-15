package com.usermanagement.core.infrastructure.repository;

import com.usermanagement.core.infrastructure.repository.entities.Phone;
import com.usermanagement.core.infrastructure.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    /**
     * Finds a list of Phone entities associated with a given User entity.
     *
     * @param user the User entity for which to find associated Phone entities.
     * @return a list of Phone entities associated with the specified User, or an empty list if none are found.
     */
    List<Phone> findByUser(User user);
}
