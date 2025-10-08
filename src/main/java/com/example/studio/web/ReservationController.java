package com.example.studio.web;

import com.example.studio.model.AppUser;
import com.example.studio.model.Reservation;
import com.example.studio.repo.AppUserRepository;
import com.example.studio.repo.ReservationRepository;
import com.example.studio.repo.ProfileRepository;
import com.example.studio.service.GoogleCalendarSyncService;
import com.example.studio.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {

    private final ReservationRepository repo;
    private final GoogleCalendarSyncService calendarSync;
    private final ProfileRepository profileRepo;
    private final AppUserRepository appUserRepo;
    private final UserService userService;

    public ReservationController(ReservationRepository repo, 
                                GoogleCalendarSyncService calendarSync,
                                ProfileRepository profileRepo,
                                AppUserRepository appUserRepo,
                                UserService userService) {
        this.repo = repo;
        this.calendarSync = calendarSync;
        this.profileRepo = profileRepo;
        this.appUserRepo = appUserRepo;
        this.userService = userService;
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        model.addAttribute("reservations", repo.findAll());
        model.addAttribute("profiles", profileRepo.findAll());
        
        List<AppUser> users = appUserRepo.findAll();
        model.addAttribute("users", users);
        
        AppUser currentUser = userService.getCurrentUser();
        Reservation reservation = new Reservation();
        if (currentUser != null) {
            reservation.setName(currentUser.getDisplayName());
        }
        model.addAttribute("reservation", reservation);
        
        return "reservations";
    }

    @PostMapping("/reservations")
    public String create(@ModelAttribute Reservation reservation) {
        if (reservation.getVenue() == null || reservation.getVenue().isEmpty()) {
            reservation.setVenue(getVenueFromRoom(reservation.getRoom()));
        }
        repo.insert(reservation);
        calendarSync.syncReservation(reservation);
        return "redirect:/manage";
    }

    @PostMapping("/reservations/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        calendarSync.deleteReservation(String.valueOf(id));
        return "redirect:/manage";
    }

    @GetMapping("/reservations/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Reservation reservation = repo.findById(id);
        model.addAttribute("reservation", reservation);
        
        List<AppUser> users = appUserRepo.findAll();
        model.addAttribute("users", users);
        
        return "edit";
    }

    @PostMapping("/reservations/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        reservation.setId(id);
        Reservation existing = repo.findById(id);
        if (existing != null) {
            reservation.setVenue(existing.getVenue());
        }
        repo.update(reservation);
        calendarSync.updateReservation(reservation);
        return "redirect:/manage";
    }

    @GetMapping("/calendar/studio")
    public String calendarStudio() {
        return "calendar-studio";
    }

    @GetMapping("/calendar/roxette")
    public String calendarRoxette() {
        return "calendar-roxette";
    }

    @GetMapping("/calendar")
    public String calendar() {
        return "redirect:/calendar/studio";
    }

    @GetMapping("/api/events")
    @ResponseBody
    public List<Map<String, Object>> getEvents(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) String room) {
        
        List<Reservation> reservations;
        if (room != null && !room.isEmpty()) {
            reservations = repo.findByRoom(room);
        } else {
            reservations = repo.findAll();
        }

        List<Map<String, Object>> events = new java.util.ArrayList<>();
        for (Reservation res : reservations) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", res.getId());
            event.put("title", res.getName());
            event.put("start", res.getStartTime().toString());
            event.put("end", res.getEndTime().toString());
            event.put("resourceId", res.getRoom());
            
            // 枠管理用フィールド
            event.put("isSlot", res.getIsSlot() != null && res.getIsSlot());
            event.put("performanceTime", res.getPerformanceTime());
            event.put("changeoverTime", res.getChangeoverTime());
            event.put("slotId", res.getSlotId());
            
            // Roxette用の拡張プロパティ
            if (res.getRepresentative() != null || res.getBandName() != null) {
                event.put("representative", res.getRepresentative());
                event.put("bandName", res.getBandName());
            }
            
            String color = "#3788d8";
            if ("A".equals(res.getRoom())) color = "#ff6b6b";
            else if ("B".equals(res.getRoom())) color = "#4ecdc4";
            else if ("Hamajirushi".equals(res.getRoom())) color = "#f7b731";
            else if ("Roxette".equals(res.getRoom())) {
                if (res.getIsSlot() != null && res.getIsSlot()) {
                    color = "#e0e0e0"; // 空き枠は薄いグレー
                } else {
                    color = "#a29bfe"; // 予約済みは紫
                }
            }
            event.put("color", color);
            
            events.add(event);
        }
        return events;
    }

    @PostMapping("/api/events")
    @ResponseBody
    public Map<String, Object> createEvent(@RequestBody Map<String, Object> payload) {
        Reservation reservation = new Reservation();
        reservation.setRoom((String) payload.get("resourceId"));
        reservation.setName((String) payload.get("name"));
        reservation.setStartTime(LocalDateTime.parse((String) payload.get("start")));
        reservation.setEndTime(LocalDateTime.parse((String) payload.get("end")));
        reservation.setVenue(getVenueFromRoom((String) payload.get("resourceId")));
        
        // 枠作成の場合
        if (payload.containsKey("isSlot") && (Boolean) payload.get("isSlot")) {
            reservation.setIsSlot(true);
            reservation.setPerformanceTime((Integer) payload.get("performanceTime"));
            reservation.setChangeoverTime((Integer) payload.get("changeoverTime"));
        }
        
        // ライブ登録の場合
        if (payload.containsKey("representative")) {
            reservation.setRepresentative((String) payload.get("representative"));
        }
        if (payload.containsKey("bandName")) {
            reservation.setBandName((String) payload.get("bandName"));
        }
        if (payload.containsKey("slotId")) {
            Object slotIdObj = payload.get("slotId");
            if (slotIdObj instanceof Number) {
                reservation.setSlotId(((Number) slotIdObj).longValue());
            }
        }
        
        repo.insert(reservation);
        calendarSync.syncReservation(reservation);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

    @PutMapping("/api/events/{id}")
    @ResponseBody
    public Map<String, Object> updateEvent(@PathVariable Long id, 
                                          @RequestBody Map<String, Object> payload) {
        Reservation reservation = repo.findById(id);
        if (reservation != null) {
            if (payload.containsKey("start")) {
                reservation.setStartTime(LocalDateTime.parse((String) payload.get("start")));
            }
            if (payload.containsKey("end")) {
                reservation.setEndTime(LocalDateTime.parse((String) payload.get("end")));
            }
            
            if (payload.containsKey("resourceId")) {
                reservation.setRoom((String) payload.get("resourceId"));
            }
            
            // 枠情報更新
            if (payload.containsKey("performanceTime")) {
                reservation.setPerformanceTime((Integer) payload.get("performanceTime"));
            }
            if (payload.containsKey("changeoverTime")) {
                reservation.setChangeoverTime((Integer) payload.get("changeoverTime"));
            }
            
            // Roxette用のフィールド更新
            if (payload.containsKey("representative")) {
                reservation.setRepresentative((String) payload.get("representative"));
            }
            if (payload.containsKey("bandName")) {
                reservation.setBandName((String) payload.get("bandName"));
            }
            if (payload.containsKey("name")) {
                reservation.setName((String) payload.get("name"));
            }
            
            repo.update(reservation);
            calendarSync.updateReservation(reservation);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

    @DeleteMapping("/api/events/{id}")
    @ResponseBody
    public Map<String, Object> deleteEvent(@PathVariable Long id) {
        Reservation reservation = repo.findById(id);
        if (reservation != null) {
            repo.deleteById(id);
            calendarSync.deleteReservation(String.valueOf(id));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

    private String getVenueFromRoom(String room) {
        if ("A".equals(room) || "B".equals(room)) return "STUDIO";
        if ("Hamajirushi".equals(room)) return "TEPPAN";
        if ("Roxette".equals(room)) return "LIVEHOUSE";
        return "STUDIO";
    }
}