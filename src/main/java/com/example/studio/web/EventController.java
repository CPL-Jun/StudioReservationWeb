package com.example.studio.web;

import com.example.studio.model.AppUser;
import com.example.studio.model.Event;
import com.example.studio.repo.EventRepository;
import com.example.studio.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    private final EventRepository eventRepo;
    private final UserService userService;

    public EventController(EventRepository eventRepo, UserService userService) {
        this.eventRepo = eventRepo;
        this.userService = userService;
    }

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventRepo.findAll());
        model.addAttribute("isAdmin", userService.isCurrentUserAdmin());
        return "events";
    }

    @GetMapping("/events/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public String createEvent(@ModelAttribute Event event) {
        // 作成者を設定
        AppUser currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            event.setCreatedBy(currentUser.getEmail());
        }
        
        eventRepo.insert(event);
        return "redirect:/events?success=created";
    }

    @GetMapping("/events/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        Event event = eventRepo.findById(id);
        model.addAttribute("event", event);
        return "event-form";
    }

    @PostMapping("/events/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event) {
        event.setId(id);
        eventRepo.update(event);
        return "redirect:/events?success=updated";
    }

    @PostMapping("/events/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEvent(@PathVariable Long id) {
        eventRepo.deleteById(id);
        return "redirect:/events?success=deleted";
    }
}