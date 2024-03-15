package com.usermanagement.core.infrastructure.controller.advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

record ResponseError(LocalDateTime date, String message, HttpStatus status, int code) {
}
