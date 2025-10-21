package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.constant.ApplicationConstants;
import com.ansar.moneymanaer_api.dto.AuthDto;
import com.ansar.moneymanaer_api.dto.ProfileDto;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.UserAlreadyExists;
import com.ansar.moneymanaer_api.repository.ProfileRepository;
import com.ansar.moneymanaer_api.util.JwtUtil;
import com.ansar.moneymanaer_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;
    @Value("${app.activation.base-url}")
    private String activationBaseUrl;


    public ProfileDto createProfile(ProfileDto profileDto) {

        Optional<ProfileEntity> profile = profileRepository.findByEmail(profileDto.getEmail());
        if (profile.isPresent()) {
            throw new UserAlreadyExists("User profile already Exits by email " + profileDto.getEmail());
        }

        ProfileEntity profileEntity = MapperUtil.dtoToProfileEntity(profileDto);
        profileEntity.setActivationToken(UUID.randomUUID().toString());

        profileEntity.setPassword(passwordEncoder.encode(profileDto.getPassword()));
        ProfileEntity savedProfileEntity = profileRepository.save(profileEntity);

        String activationLink = activationBaseUrl + "?token=" + profileEntity.getActivationToken();
        String subject = ApplicationConstants.SUBJECT;
        String body = """
                Dear %s,
                
                Thank you for registering with Money Manager!
                
                Please activate your account by clicking the link below:
                %s
                
                If you did not request this, please ignore this email.
                
                Regards,
                Money Manager Team
                """.formatted(savedProfileEntity.getFullName(), activationLink);

        emailService.sendEmail(profileEntity.getEmail(), subject, body);

        log.info("User Profile created successfully with email {} ", savedProfileEntity.getEmail());

        return MapperUtil.profileEntityToDto(savedProfileEntity);
    }

    public boolean activateProfile(String activationToken) {

        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    public boolean isActive(String email) {
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found wit email " + authentication.getName()));
    }

    public ProfileDto getPublicProfile(String email) {

        ProfileEntity currentProfile = null;
        if (email == null) {
            currentProfile = getCurrentProfile();
        } else {
            currentProfile = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email " + email));
        }
        return MapperUtil.profileEntityToDto(currentProfile);
    }

    public Map<String, Object> authenticationAndGenerateToken(AuthDto authDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authDto.getEmail());
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            return Map.of("accessToken", accessToken,
                    "refreshToken",refreshToken,
                    "user", getPublicProfile(authDto.getEmail()));
        } catch (Exception e) {

            throw new RuntimeException("Invalid email or password");
        }
    }
}
