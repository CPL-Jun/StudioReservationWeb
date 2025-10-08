package com.example.studio.web;

import com.example.studio.model.AppUser;
import com.example.studio.model.HamajirushiParticipant;
import com.example.studio.model.UserProfile;
import com.example.studio.repo.HamajirushiParticipantRepository;
import com.example.studio.repo.UserProfileRepository;
import com.example.studio.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HamajirushiController {

    private final HamajirushiParticipantRepository participantRepo;
    private final UserProfileRepository profileRepo;
    private final UserService userService;

    public HamajirushiController(HamajirushiParticipantRepository participantRepo,
                                 UserProfileRepository profileRepo,
                                 UserService userService) {
        this.participantRepo = participantRepo;
        this.profileRepo = profileRepo;
        this.userService = userService;
    }

    @GetMapping("/calendar/hamajirushi")
    public String hamajirushiCalendar(@RequestParam(required = false) String month, Model model) {
        AppUser currentUser = userService.getCurrentUser();
        
        // 月の指定がない場合は今月
        YearMonth targetMonth;
        if (month != null && !month.isEmpty()) {
            targetMonth = YearMonth.parse(month);
        } else {
            targetMonth = YearMonth.now();
        }
        
        // 前月・翌月
        YearMonth prevMonth = targetMonth.minusMonths(1);
        YearMonth nextMonth = targetMonth.plusMonths(1);
        
        // その月の参加者データを取得
        List<HamajirushiParticipant> participants = participantRepo.findByMonth(
            targetMonth.getYear(), targetMonth.getMonthValue());
        
        // 日付ごとの参加者数をカウント
        Map<String, Integer> countByDate = new HashMap<>();
        Map<String, Boolean> userParticipating = new HashMap<>();
        
        for (HamajirushiParticipant p : participants) {
            String dateStr = p.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            countByDate.put(dateStr, countByDate.getOrDefault(dateStr, 0) + 1);
            
            if (currentUser != null && p.getUserId().equals(currentUser.getId())) {
                userParticipating.put(dateStr, true);
            }
        }
        
        model.addAttribute("currentMonth", targetMonth);
        model.addAttribute("prevMonth", prevMonth);
        model.addAttribute("nextMonth", nextMonth);
        model.addAttribute("countByDate", countByDate);
        model.addAttribute("userParticipating", userParticipating);
        model.addAttribute("currentUser", currentUser);
        
        return "calendar-hamajirushi";
    }

    @GetMapping("/hamajirushi/participants/{date}")
    public String showParticipants(@PathVariable String date, Model model) {
        LocalDate targetDate = LocalDate.parse(date);
        List<HamajirushiParticipant> participants = participantRepo.findByDate(targetDate);
        
        // 参加者のプロフィール情報を取得
        Map<Long, UserProfile> profiles = new HashMap<>();
        for (HamajirushiParticipant p : participants) {
            UserProfile profile = profileRepo.findByUserId(p.getUserId());
            if (profile != null) {
                profiles.put(p.getUserId(), profile);
            }
        }
        
        model.addAttribute("date", targetDate);
        model.addAttribute("participants", participants);
        model.addAttribute("profiles", profiles);
        
        return "hamajirushi-participants";
    }

    @PostMapping("/hamajirushi/participate")
    @ResponseBody
    public Map<String, Object> participate(@RequestParam String date) {
        Map<String, Object> response = new HashMap<>();
        
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            response.put("status", "error");
            response.put("message", "ログインが必要です");
            return response;
        }
        
        LocalDate targetDate = LocalDate.parse(date);
        
        // すでに参加している場合は削除（トグル）
        if (participantRepo.existsByDateAndUserId(targetDate, currentUser.getId())) {
            participantRepo.deleteByDateAndUserId(targetDate, currentUser.getId());
            response.put("status", "removed");
            response.put("message", "参加をキャンセルしました");
        } else {
            participantRepo.insert(targetDate, currentUser.getId());
            response.put("status", "added");
            response.put("message", "参加を登録しました");
        }
        
        response.put("count", participantRepo.countByDate(targetDate));
        return response;
    }

    @GetMapping("/api/hamajirushi/count/{date}")
    @ResponseBody
    public Map<String, Object> getCount(@PathVariable String date) {
        Map<String, Object> response = new HashMap<>();
        LocalDate targetDate = LocalDate.parse(date);
        
        response.put("count", participantRepo.countByDate(targetDate));
        
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            response.put("participating", participantRepo.existsByDateAndUserId(targetDate, currentUser.getId()));
        }
        
        return response;
    }
}