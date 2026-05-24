package com.edwinkesuma.springedmastore.infrastructure.security;

import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String email = authentication.getName();

        if (email == null || email.isBlank()) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Object credentials = authentication.getCredentials();

        if (credentials == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String pwd = credentials.toString();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (user.getRole() == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        boolean matches = passwordEncoder.matches(pwd, user.getPasswordHash());

        if (!matches) {
            throw new BadCredentialsException("Invalid email or password");
        }

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
