package com.nepath.carapp.security.extensions;

import com.nepath.carapp.models.User;
import com.nepath.carapp.security.IdUsernamePasswordAuthenticationToken;
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

    public static User getUserWithOnlyIdSet() {
        User user = new User();
        user.setId(getUserId());
        return user;
    }

    public static User getUserWithOnlyNameSet() {
        User user = new User();
        user.setNick(getUserName());
        return user;
    }
}
