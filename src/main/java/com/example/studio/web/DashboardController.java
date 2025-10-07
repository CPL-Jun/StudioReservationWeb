package com.example.studio.web;

import com.example.studio.model.AppUser;
import com.example.studio.model.Reservation;
import com.example.studio.repo.ReservationRepository;
import com.example.studio.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Controller
public class DashboardController {

    private final ReservationRepository reservationRepo;
    private final UserService userService;

    public DashboardController(ReservationRepository reservationRepo, UserService userService) {
        this.reservationRepo = reservationRepo;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AppUser currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        
        boolean isAdmin = currentUser != null && "ROLE_ADMIN".equals(currentUser.getRole());
        model.addAttribute("isAdmin", isAdmin);
        
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        // 今日の集計
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        
        List<Reservation> todayReservations = reservationRepo.findAll().stream()
            .filter(r -> !r.getStartTime().isBefore(startOfDay) && r.getStartTime().isBefore(endOfDay))
            .filter(r -> !"Roxette".equals(r.getRoom())) // Roxetteを除外
            .toList();
        
        long todaySales = calculateSales(todayReservations);
        int todayCount = todayReservations.size();
        
        // 今月の集計
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        
        List<Reservation> monthReservations = reservationRepo.findAll().stream()
            .filter(r -> !r.getStartTime().isBefore(startOfMonth) && r.getStartTime().isBefore(endOfMonth))
            .filter(r -> !"Roxette".equals(r.getRoom())) // Roxetteを除外
            .toList();
        
        long monthSales = calculateSales(monthReservations);
        int monthCount = monthReservations.size();
        
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("todayCount", todayCount);
        model.addAttribute("monthSales", monthSales);
        model.addAttribute("monthCount", monthCount);
        
        return "admin";
    }

    private long calculateSales(List<Reservation> reservations) {
        return reservations.stream()
            .mapToLong(r -> {
                Duration duration = Duration.between(r.getStartTime(), r.getEndTime());
                long hours = duration.toHours();
                return hours * 1200; // 1時間1200円
            })
            .sum();
    }
}