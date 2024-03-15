package com.usermanagement.core.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Phone {

    @Id
    @GeneratedValue
    private Long id;
    private Long number;
    private Integer cityCode;
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
