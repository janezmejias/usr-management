package com.usermanagement.core.infrastructure.controller;

import com.usermanagement.core.domain.dto.SignUpResponse;
import com.usermanagement.core.domain.dto.UserRequest;
import com.usermanagement.core.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.create(userRequest));
    }

    @GetMapping("/login")
    public ResponseEntity<SignUpResponse> login() {
        return ResponseEntity.ok(userService.syncToken());
    }

}
