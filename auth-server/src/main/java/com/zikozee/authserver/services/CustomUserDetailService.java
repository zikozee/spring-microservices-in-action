package com.zikozee.authserver.services;

import com.zikozee.authserver.entity.User;
import com.zikozee.authserver.model.SecurityUser;
import com.zikozee.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        log.info("user:: -> {}", user.orElse(null));
        return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException(":("));
    }
}
