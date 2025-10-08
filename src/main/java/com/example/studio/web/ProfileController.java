package com.example.studio.web;

import com.example.studio.model.Profile;
import com.example.studio.repo.ProfileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final ProfileRepository profileRepo;

    public ProfileController(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    @GetMapping("/profiles")
    public String profiles(Model model) {
        model.addAttribute("profiles", profileRepo.findAll());
        model.addAttribute("newProfile", new Profile());
        return "profiles";
    }

    @PostMapping("/profiles")
    public String createProfile(@ModelAttribute Profile profile) {
        profileRepo.insert(profile);
        return "redirect:/profiles";
    }

    @PostMapping("/profiles/{id}/delete")
    public String deleteProfile(@PathVariable Long id) {
        profileRepo.deleteById(id);
        return "redirect:/profiles";
    }
}