package com.usermanagement.core.domain.dto;

public record PhoneRequest(
        Long number,
        Integer cityCode,
        String countryCode) {
}
