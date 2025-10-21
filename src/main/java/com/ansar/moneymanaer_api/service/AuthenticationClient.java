package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.client.AuthRequest;
import com.ansar.moneymanaer_api.client.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "authentication",url = "${authenticate.url}")
public interface AuthenticationClient {

    @PostMapping(
            value = "/login",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest);

}
