package org.GG.apcProject.service;

import org.GG.apcProject.model.User;
import org.GG.apcProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Set<GrantedAuthority> authorities = u.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                authorities
        );
    }
}
