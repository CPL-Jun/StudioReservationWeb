package com.example.studio.service;

import com.example.studio.model.AppUser;
import com.example.studio.repo.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepo;

    public AppUserDetailsService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepo.findByEmail(email);
        
        if (appUser == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + email);
        }

        return User.builder()
            .username(appUser.getEmail())
            .password(appUser.getPasswordHash())
            .roles(appUser.getRole().replace("ROLE_", ""))
            .build();
    }
}