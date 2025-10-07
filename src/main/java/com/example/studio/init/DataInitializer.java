package com.example.studio.init;

import com.example.studio.model.AppUser;
import com.example.studio.repo.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // テストユーザーの作成
        AppUser testUser = userRepo.findByEmail("test@example.com");
        if (testUser == null) {
            userRepo.insert(
                "test@example.com",
                passwordEncoder.encode("password"),
                "テストユーザー",
                "ROLE_USER"
            );
            System.out.println("テストユーザーを作成しました: test@example.com / password");
        }

        // 管理者ユーザーの作成
        AppUser adminUser = userRepo.findByEmail("admin@example.com");
        if (adminUser == null) {
            userRepo.insert(
                "admin@example.com",
                passwordEncoder.encode("admin"),
                "管理者",
                "ROLE_ADMIN"
            );
            System.out.println("管理者ユーザーを作成しました: admin@example.com / admin");
        }
    }
}