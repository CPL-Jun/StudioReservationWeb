package com.example.studio.web;

import com.example.studio.dto.ProfileEditForm;
import com.example.studio.model.AppUser;
import com.example.studio.model.UserProfile;
import com.example.studio.repo.UserProfileRepository;
import com.example.studio.service.UserService;
import com.example.studio.constant.InstrumentConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserProfileController {

    private final UserProfileRepository profileRepo;
    private final UserService userService;

    public UserProfileController(UserProfileRepository profileRepo, UserService userService) {
        this.profileRepo = profileRepo;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        UserProfile profile = profileRepo.findByUserId(currentUser.getId());
        model.addAttribute("user", currentUser);
        model.addAttribute("profile", profile);
        return "user-profile";
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
            
            // Stringのまま設定
            form.setFavoriteBands(profile.getFavoriteBands());
            form.setGender(profile.getGender());
            form.setAge(profile.getAge());
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
}