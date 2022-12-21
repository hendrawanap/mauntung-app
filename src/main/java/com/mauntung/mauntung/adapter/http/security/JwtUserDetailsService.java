package com.mauntung.mauntung.adapter.http.security;

import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(NullPointerException::new);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            if (user.getRole().equals(User.Role.MERCHANT)) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_MERCHANT"));
            } else if (user.getRole().equals(User.Role.CUSTOMER)) {
                authorityList.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            }
            return new UserDetailsImpl(user.getId(), user.getEmail(), user.getEmail(), user.getPassword(), authorityList);
        } catch (NullPointerException e) {
            throw new UsernameNotFoundException("User Not Found");
        }
    }
}
