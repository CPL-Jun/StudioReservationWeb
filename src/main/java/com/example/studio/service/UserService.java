package com.example.studio.service;

import com.example.studio.dto.UserRegistrationForm;
import com.example.studio.model.AppUser;
import com.example.studio.repo.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 現在ログイン中のユーザーを取得
     */
    public AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        
        String email = auth.getName();
        return userRepo.findByEmail(email);
    }

    /**
     * メールアドレスでユーザーを検索
     */
    public AppUser findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    /**
     * 新規ユーザー登録
     */
    public void registerNewUser(UserRegistrationForm form) {
        String hashedPassword = passwordEncoder.encode(form.getPassword());
        userRepo.insert(form.getEmail(), hashedPassword, form.getDisplayName(), "ROLE_USER");
    }

    /**
     * ユーザー情報更新
     */
    public void updateUser(AppUser user) {
        userRepo.update(user);
    }

    /**
     * 現在のユーザーが管理者かどうか
     */
    public boolean isCurrentUserAdmin() {
        AppUser currentUser = getCurrentUser();
        return currentUser != null && "ROLE_ADMIN".equals(currentUser.getRole());
    }
}