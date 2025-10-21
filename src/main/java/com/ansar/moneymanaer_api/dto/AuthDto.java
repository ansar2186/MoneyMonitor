package com.ansar.moneymanaer_api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class AuthDto {
    private String email;
    private String password;
    private String token;
}
