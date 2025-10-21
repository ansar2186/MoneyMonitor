package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.client.AuthRequest;
import com.ansar.moneymanaer_api.client.AuthResponse;
import com.ansar.moneymanaer_api.service.AuthenticationClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final AuthenticationClient authenticationClient;

    @PostMapping("/authLogin")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            ResponseEntity<AuthResponse> response = authenticationClient.login(authRequest);
            System.out.println("email :" +authRequest.getEmail() +" " +"Password :" +authRequest.getPassword());

            if (response.getStatusCode().is2xxSuccessful()) {
                // ✅ Successful login
                AuthResponse body = response.getBody();
                return ResponseEntity.ok(body);
            } else {
                // ❌ Non-200 but no exception (unlikely)
                return ResponseEntity.status(response.getStatusCode()).body("Unexpected error");
            }

        } catch (feign.FeignException.BadRequest ex) {
            // 🔒 400 Bad Credentials
            return ResponseEntity.badRequest().body("Invalid email or password");
        } catch (feign.FeignException.Unauthorized ex) {
            // 🔒 401 Disabled user or general failure
            return ResponseEntity.status(401).body("Unauthorized: " + ex.getMessage());
        } catch (feign.FeignException ex) {
            // 🔥 Any other Feign-related error
            return ResponseEntity.status(ex.status()).body("Feign error: " + ex.getMessage());
        } catch (Exception ex) {
            // 🔥 Fallback
            return ResponseEntity.internalServerError().body("Server error: " + ex.getMessage());
        }

    }
}
