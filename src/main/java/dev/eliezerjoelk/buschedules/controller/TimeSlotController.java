package dev.eliezerjoelk.buschedules.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.GeneticAlgorithmService;
import dev.eliezerjoelk.buschedules.service.TimeSlotService;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;
    
    @Autowired
    private GeneticAlgorithmService geneticSchedulingService;

    /**
     * Get all possible time slot configurations
     */
    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        return ResponseEntity.ok(timeSlotService.getAllTimeSlots());
    }

    /**
     * Get time slots for a specific day
     */
    // @GetMapping("/by-day")
    // public ResponseEntity<List<TimeSlot>> getTimeSlotsForDay(
    //         @RequestParam DayOfWeek dayOfWeek) {
    //     return ResponseEntity.ok(timeSlotService.getAllTimeSlots().stream()
    //         .filter(slot -> slot.getDayOfWeek() == dayOfWeek)
    //         .collect(Collectors.toList()));
    // }

    /**
     * Get time slots within a time range
    //  */
    // @GetMapping("/by-time-range")
    // public ResponseEntity<List<TimeSlot>> getTimeSlotsInTimeRange(
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
    //     return ResponseEntity.ok(timeSlotService.getAllTimeSlots().stream()
    //         .filter(slot -> !slot.getStartTime().isBefore(startTime) && 
    //                       !slot.getEndTime().isAfter(endTime))
    //         .collect(Collectors.toList()));
    // }

    /**
     * Generate optimal schedule using genetic algorithm
     */
    @PostMapping("/generate-schedule")
    public ResponseEntity<List<ScheduledClass>> generateOptimalSchedule() {
        return ResponseEntity.ok(geneticSchedulingService.generateSchedule());
    }

    /**
     * Configure time slot parameters (admin only)
     */
    @PostMapping("/configure")
    public ResponseEntity<Map<String, Object>> configureTimeSlots(
            @RequestBody TimeSlotConfigRequest config) {
        timeSlotService.configureTimeSlots(
            config.getStartOfDay(), 
            config.getEndOfDay(),
            config.getSlotDurationMinutes());
        
        return ResponseEntity.ok(Map.of(
            "message", "Time slot configuration updated successfully",
            "config", config
        ));
    }

    /**
     * Request object for time slot configuration
     */
    public static class TimeSlotConfigRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime startOfDay;
        
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime endOfDay;
        
        private int slotDurationMinutes;

        // Getters and setters
        public LocalTime getStartOfDay() {
            return startOfDay;
        }

        public void setStartOfDay(LocalTime startOfDay) {
            this.startOfDay = startOfDay;
        }

        public LocalTime getEndOfDay() {
            return endOfDay;
        }

        public void setEndOfDay(LocalTime endOfDay) {
            this.endOfDay = endOfDay;
        }

        public int getSlotDurationMinutes() {
            return slotDurationMinutes;
        }

        public void setSlotDurationMinutes(int slotDurationMinutes) {
            this.slotDurationMinutes = slotDurationMinutes;
        }
    }
}