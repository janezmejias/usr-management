package com.usermanagement.core.domain.mapper;

import com.usermanagement.core.domain.dto.SignUpResponse;
import com.usermanagement.core.domain.dto.UserRequest;
import com.usermanagement.core.infrastructure.repository.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Converts a UserRequest DTO to a User entity. Automatically sets the 'created', 'modified', and
     * 'lastLogin' fields to the current LocalDateTime. The 'isActive' field is set to true by default.
     *
     * @param dto the UserRequest DTO containing data to be mapped to a User entity.
     * @return a User entity populated with the data from the DTO and additional automatic fields.
     */
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "modified", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", expression = "java(Boolean.TRUE)")
    User with(UserRequest dto);

    /**
     * Converts a User entity to a SignUpResponse DTO. This method is intended for mapping the
     * essential information from a persisted User entity back into a DTO format suitable for
     * client responses, especially after user registration or update operations.
     *
     * @param user the User entity to be converted into a SignUpResponse DTO.
     * @return a SignUpResponse DTO containing relevant information extracted from the User entity.
     */
    SignUpResponse with(User user);
}
