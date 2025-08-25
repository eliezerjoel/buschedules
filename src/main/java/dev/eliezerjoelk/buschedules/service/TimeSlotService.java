package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;

import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TimeSlotService {
    
    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    
    @Value("${timetable.start-time:08:00}")
    private String startTimeStr;
    
    @Value("${timetable.end-time:18:00}")
    private String endTimeStr;
    
    @Value("${timetable.slot-duration-minutes:60}")
    private int slotDurationMinutes;
    
    @Value("${timetable.break-duration-minutes:0}")
    private int breakDurationMinutes;
    
    /**
     * Generate all available time slots for genetic algorithm
     * This is the primary method used by the GA to get all possible scheduling options
     */
    public List<TimeSlot> generateAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        
        LocalTime startOfDay = LocalTime.parse(startTimeStr);
        LocalTime endOfDay = LocalTime.parse(endTimeStr);
        
        // Generate slots for weekdays only (Monday to Friday)
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                continue;
            }
            
            timeSlots.addAll(generateTimeSlotsForDay(day, startOfDay, endOfDay));
        }
        
        return timeSlots;
    }
    
    /**
     * Generate time slots for a specific day
     */
    public List<TimeSlot> generateTimeSlotsForDay(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> daySlots = new ArrayList<>();
        LocalTime currentTime = startTime;
        
        while (currentTime.plusMinutes(slotDurationMinutes).compareTo(endTime) <= 0) {
            LocalTime slotEndTime = currentTime.plusMinutes(slotDurationMinutes);
            
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDayOfWeek(dayOfWeek);
            timeSlot.setStartTime(currentTime);
            timeSlot.setEndTime(slotEndTime);
            
            daySlots.add(timeSlot);
            
            currentTime = slotEndTime.plusMinutes(breakDurationMinutes);
        }
        
        return daySlots;
    }
    
    /**
     * Get time slots for specific days (useful for filtering)
     */
    public List<TimeSlot> getTimeSlotsForDays(List<DayOfWeek> days) {
        return generateAllTimeSlots().stream()
            .filter(slot -> days.contains(slot.getDayOfWeek()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get time slots within a specific time range
     */
    public List<TimeSlot> getTimeSlotsInRange(LocalTime startTime, LocalTime endTime) {
        return generateAllTimeSlots().stream()
            .filter(slot -> !slot.getStartTime().isBefore(startTime) && 
                           !slot.getEndTime().isAfter(endTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Get morning time slots (before 12:00 PM)
     */
    public List<TimeSlot> getMorningTimeSlots() {
        return getTimeSlotsInRange(LocalTime.parse(startTimeStr), LocalTime.of(12, 0));
    }
    
    /**
     * Get afternoon time slots (after 12:00 PM)
     */
    public List<TimeSlot> getAfternoonTimeSlots() {
        return getTimeSlotsInRange(LocalTime.of(12, 0), LocalTime.parse(endTimeStr));
    }
    
    /**
     * Check if two time slots overlap (utility method for GA fitness evaluation)
     */
    public boolean timeSlotsOverlap(TimeSlot slot1, TimeSlot slot2) {
        if (!slot1.getDayOfWeek().equals(slot2.getDayOfWeek())) {
            return false;
        }
        
        return slot1.getStartTime().isBefore(slot2.getEndTime()) && 
               slot2.getStartTime().isBefore(slot1.getEndTime());
    }
    
    /**
     * Get total number of available time slots per day
     */
    public int getTimeSlotsPerDay() {
        LocalTime startOfDay = LocalTime.parse(startTimeStr);
        LocalTime endOfDay = LocalTime.parse(endTimeStr);
        
        int totalMinutes = (endOfDay.getHour() - startOfDay.getHour()) * 60 + 
                          (endOfDay.getMinute() - startOfDay.getMinute());
        
        return totalMinutes / (slotDurationMinutes + breakDurationMinutes);
    }
    
    /**
     * Get total number of available time slots for the week
     */
    public int getTotalWeeklyTimeSlots() {
        return getTimeSlotsPerDay() * 5; // 5 weekdays
    }
    
    /**
     * Create a time slot with specific parameters (utility method)
     */
    public TimeSlot createTimeSlot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDayOfWeek(dayOfWeek);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        return timeSlot;
    }
    public List<TimeSlot> getAllTimeSlots() {
    return generateAllTimeSlots();
}


public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
    List<TimeSlot> allSlots = getAllTimeSlots();

    // Fetch scheduled classes for the course and instructor
    List<ScheduledClass> courseClasses = scheduledClassRepository.findByCourseId(courseId);
    List<ScheduledClass> instructorClasses = scheduledClassRepository.findByInstructorId(instructorId);

    return allSlots.stream()
        .filter(slot -> courseClasses.stream().noneMatch(sc -> timeSlotsOverlap(slot, sc.getTimeSlot())) &&
                        instructorClasses.stream().noneMatch(sc -> timeSlotsOverlap(slot, sc.getTimeSlot())))
        .collect(Collectors.toList());
}


// public boolean isTimeSlotAvailable(String courseId, String instructorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
//     // Create a TimeSlot object for comparison
//     TimeSlot requestedSlot = createTimeSlot(dayOfWeek, startTime, endTime);

//     // Fetch scheduled classes for the course and instructor
//     List<ScheduledClass> courseClasses = scheduledClassRepository.findByCourseId(courseId);
//     List<ScheduledClass> instructorClasses = scheduledClassRepository.findByInstructorId(instructorId);

//     // Check for overlap with course's scheduled classes
//     boolean courseConflict = courseClasses.stream()
//         .anyMatch(sc -> timeSlotsOverlap(requestedSlot, sc.getTimeSlot()));

//     // Check for overlap with instructor's scheduled classes
//     boolean instructorConflict = instructorClasses.stream()
//         .anyMatch(sc -> timeSlotsOverlap(requestedSlot, sc.getTimeSlot()));

//     // Slot is available if there are no conflicts
//     return !courseConflict && !instructorConflict;
// }
    /**
     * Get configuration summary for logging/debugging
     */
    public String getConfigurationSummary() {
        return String.format("TimeSlot Configuration: %s-%s, %d min slots, %d min breaks, %d total weekly slots",
            startTimeStr, endTimeStr, slotDurationMinutes, breakDurationMinutes, getTotalWeeklyTimeSlots());
    }
}
