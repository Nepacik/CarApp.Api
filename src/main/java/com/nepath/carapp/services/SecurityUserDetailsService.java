package com.nepath.carapp.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityUserDetailsService extends UserDetailsService {
    void saveToken(Long userId, String refreshToken) throws InterruptedException;
}
