package com.nepath.carapp.security.extensions;

import com.nepath.carapp.security.models.IdUsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
    public static Long getUserId() {
        return getCurrentSecurityUserDetails().getUserId();
    }

    public static IdUsernamePasswordAuthenticationToken getCurrentSecurityUserDetails() {
        return (IdUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUserName() {
        return getCurrentSecurityUserDetails().getName();
    }
}
