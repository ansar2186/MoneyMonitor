package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity exitsProfile = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email " + email));

        return User.builder()
                .username(exitsProfile.getEmail())
                .password(exitsProfile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
