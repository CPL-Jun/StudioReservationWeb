package com.example.studio.web;

import com.example.studio.model.AppUser;
import com.example.studio.model.BbsPost;
import com.example.studio.repo.BbsRepository;
import com.example.studio.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BbsController {

    private final BbsRepository bbsRepo;
    private final UserService userService;

    public BbsController(BbsRepository bbsRepo, UserService userService) {
        this.bbsRepo = bbsRepo;
        this.userService = userService;
    }

    @GetMapping("/bbs")
    public String bbs(Model model) {
        model.addAttribute("posts", bbsRepo.findAll());
        model.addAttribute("newPost", new BbsPost());
        return "bbs";
    }

    @PostMapping("/bbs")
    public String createPost(@ModelAttribute BbsPost post) {
        // 現在ログイン中のユーザー名を取得
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            post.setName(currentUser.getDisplayName());
        } else {
            post.setName("匿名");
        }
        
        bbsRepo.insert(post);
        return "redirect:/bbs";
    }
}