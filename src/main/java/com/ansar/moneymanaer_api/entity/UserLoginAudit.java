package com.ansar.moneymanaer_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Setter
public class UserLoginAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String ipAddress;
    private String country;
    private String city;
    private String userAgent;
    private String loginStatus;
    private LocalDateTime loginTime;


}
