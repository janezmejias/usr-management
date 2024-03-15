package com.usermanagement.core.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserRequest(UUID id, String name,
                          @Email(message = "El formato del correo electrónico no es válido")
                          @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                  message = "El formato del correo electrónico no es válido")
                          String email,
                          @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,12}$",
                                  message = "La contraseña debe tener un mínimo de 8 caracteres y un máximo de 12, solo una letra mayúscula y sólo dos dígitos..")
                          String password,
                          List<PhoneRequest> phones, LocalDateTime created, LocalDateTime lastLogin) {
}
