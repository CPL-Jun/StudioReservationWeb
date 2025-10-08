package com.example.studio.web;

import com.example.studio.constant.InstrumentConstants;
import com.example.studio.dto.ProfileEditForm;
import com.example.studio.model.AppUser;
import com.example.studio.model.UserProfile;
import com.example.studio.repo.AppUserRepository;
import com.example.studio.repo.UserProfileRepository;
import com.example.studio.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final AppUserRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final UserService userService;

    public ProfileController(AppUserRepository userRepo, UserProfileRepository profileRepo, UserService userService) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        // 自分のプロフィールを取得
        UserProfile myProfile = profileRepo.findByUserId(currentUser.getId());
        
        // 全ユーザーとプロフィールを取得（自分以外）
        List<AppUser> allUsers = userRepo.findAll();
        Map<AppUser, UserProfile> otherProfiles = new HashMap<>();
        
        for (AppUser user : allUsers) {
            if (!user.getId().equals(currentUser.getId())) {
                UserProfile profile = profileRepo.findByUserId(user.getId());
                otherProfiles.put(user, profile);
            }
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("profile", myProfile);
        model.addAttribute("otherProfiles", otherProfiles);
        
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editForm(Model model) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        UserProfile profile = profileRepo.findByUserId(currentUser.getId());
        
        ProfileEditForm form = new ProfileEditForm();
        if (profile != null) {
            form.setMainInstrument(profile.getMainInstrument());
            
            // String[]に変換
            String subInstruments = profile.getSubInstruments();
            if (subInstruments != null && !subInstruments.isEmpty()) {
                form.setSubInstruments(subInstruments.split(","));
            }
            
            form.setFavoriteBands(profile.getFavoriteBands());
            form.setGender(profile.getGender());
            form.setAge(profile.getAge());
            form.setComment(profile.getComment());
        }
        
        model.addAttribute("form", form);
        model.addAttribute("availableInstruments", InstrumentConstants.AVAILABLE_INSTRUMENTS);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute ProfileEditForm form) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        UserProfile profile = profileRepo.findByUserId(currentUser.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(currentUser.getId());
        }
        
        profile.setMainInstrument(form.getMainInstrument());
        
        // String[]をカンマ区切り文字列に変換
        if (form.getSubInstruments() != null && form.getSubInstruments().length > 0) {
            profile.setSubInstruments(String.join(",", form.getSubInstruments()));
        } else {
            profile.setSubInstruments("");
        }
        
        profile.setFavoriteBands(form.getFavoriteBands());
        profile.setGender(form.getGender());
        profile.setAge(form.getAge());
        
        if (profile.getId() == null) {
            profileRepo.insert(profile);
        } else {
            profileRepo.update(profile);
        }
        
        return "redirect:/profile?success=updated";
    }

    // 旧URLからのリダイレクト
    @GetMapping("/profiles")
    public String profilesRedirect() {
        return "redirect:/profile";
    }
}