package com.ansar.moneymanaer_api.controller;
import com.ansar.moneymanaer_api.dto.AuthDto;
import com.ansar.moneymanaer_api.dto.ProfileDto;
import com.ansar.moneymanaer_api.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto) {
        ProfileDto profile = profileService.createProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivate = profileService.activateProfile(token);
        if (isActivate) {
            return ResponseEntity.ok("Profile activate successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDto authDto) {

        try {
            if (!profileService.isActive(authDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account is not active ,Please  activate your account first "));
            }

            Map<String, Object> response = profileService.authenticationAndGenerateToken(authDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/hello")
    public String helloWorld(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Hello " +auth.getName();
    }
}
