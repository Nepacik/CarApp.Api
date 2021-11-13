package com.nepath.carapp.services.implementation;

import com.nepath.carapp.models.User;
import com.nepath.carapp.repositories.UserRepository;
import com.nepath.carapp.security.models.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNick(username);
        if (user == null) {
            log.error("user not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new SecurityUserDetails(user.getId(), user.getNick(), user.getPassword(), user.getRole().getName());
    }
}
