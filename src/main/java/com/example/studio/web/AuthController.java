package com.example.studio.web;

import com.example.studio.dto.UserRegistrationForm;
import com.example.studio.model.AppUser;
import com.example.studio.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません。");
        }
        if (logout != null) {
            model.addAttribute("message", "ログアウトしました。");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new UserRegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") UserRegistrationForm form,
                          BindingResult result,
                          Model model) {
        
        // メールアドレスの重複チェック
        AppUser existingUser = userRepo.findByEmail(form.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", "error.form", "このメールアドレスは既に登録されています。");
        }

        if (result.hasErrors()) {
            return "register";
        }

        // 新規ユーザー作成
        AppUser newUser = new AppUser();
        newUser.setEmail(form.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        newUser.setDisplayName(form.getDisplayName());
        newUser.setRole("ROLE_USER");
        
        userRepo.save(newUser);

        model.addAttribute("success", "登録が完了しました。ログインしてください。");
        return "login";
    }
}