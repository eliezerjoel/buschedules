package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

@Service
public class TimeSlotService {
    // Keep only the time slot generation methods
    private LocalTime startOfDay = LocalTime.of(8, 0);
    private LocalTime endOfDay = LocalTime.of(18, 0);
    private int slotDurationMinutes = 60;

    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) continue;
            
            LocalTime currentTime = startOfDay;
            while (currentTime.plusMinutes(slotDurationMinutes).compareTo(endOfDay) <= 0) {
                LocalTime slotEndTime = currentTime.plusMinutes(slotDurationMinutes);
                timeSlots.add(new TimeSlot(day, currentTime, slotEndTime));
                currentTime = slotEndTime;
            }
        }
        return timeSlots;
    }

    public void configureTimeSlots(LocalTime startOfDay, LocalTime endOfDay, int slotDurationMinutes) {
        this.startOfDay = startOfDay;
        this.endOfDay = endOfDay;
        this.slotDurationMinutes = slotDurationMinutes;
    }
}