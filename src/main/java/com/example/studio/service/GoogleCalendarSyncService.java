package com.example.studio.service;

import com.example.studio.model.Reservation;
import org.springframework.stereotype.Service;

/**
 * Google Calendar API連携サービス（将来実装予定）
 */
@Service
public class GoogleCalendarSyncService {
    
    public void syncReservation(Reservation reservation) {
        // TODO: 将来的にGoogle Calendar APIと連携
        System.out.println("Google Calendar sync: " + reservation.getName());
    }
    
    public void deleteReservation(String calendarEventId) {
        // TODO: 将来的にGoogle Calendarから削除
        System.out.println("Google Calendar delete: " + calendarEventId);
    }
    
    public void updateReservation(Reservation reservation) {
        // TODO: 将来的にGoogle Calendarを更新
        System.out.println("Google Calendar update: " + reservation.getName());
    }
}