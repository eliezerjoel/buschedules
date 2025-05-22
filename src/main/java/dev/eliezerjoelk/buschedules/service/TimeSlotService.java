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
    
    // Default time slot configuration
    private LocalTime startOfDay = LocalTime.of(8, 0); // 8 AM
    private LocalTime endOfDay = LocalTime.of(18, 0);  // 6 PM
    private int slotDurationMinutes = 60; // 1-hour slots
    
    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    
    /**
     * Generate all possible time slots based on current configuration
     */
    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        
        // Generate slots for each day of the week (Monday to Friday)
        for (DayOfWeek day : DayOfWeek.values()) {
            // Skip weekend days
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                continue;
            }
            
            // Generate slots throughout the day
            LocalTime currentTime = startOfDay;
            while (currentTime.plusMinutes(slotDurationMinutes).compareTo(endOfDay) <= 0) {
                LocalTime slotEndTime = currentTime.plusMinutes(slotDurationMinutes);
                timeSlots.add(new TimeSlot(day, currentTime, slotEndTime));
                currentTime = slotEndTime;
            }
        }
        
        return timeSlots;
    }
    
    /**
     * Get available time slots for a specific course and lecturer
     */
    public List<TimeSlot> getAvailableTimeSlots(String courseId, String lecturerId) {
        // Get existing assignments for this lecturer
        List<ScheduledClass> lecturerAssignments = scheduledClassRepository.findByInstructorId(lecturerId);
        
        // Get existing assignments for this course
        List<ScheduledClass> courseAssignments = scheduledClassRepository.findByCourseId(courseId);
        
        // Generate all possible time slots
        List<TimeSlot> allTimeSlots = getAllTimeSlots();
        
        // Filter out time slots that conflict with existing assignments
        return allTimeSlots.stream()
            .filter(slot -> !conflictsWithExistingAssignments(slot, lecturerAssignments, courseAssignments))
            .collect(Collectors.toList());
    }
    
    /**
     * Get time slots for a specific day of the week
     */
    public List<TimeSlot> getTimeSlotsForDay(DayOfWeek dayOfWeek) {
        return getAllTimeSlots().stream()
            .filter(slot -> slot.getDayOfWeek() == dayOfWeek)
            .collect(Collectors.toList());
    }
    
    /**
     * Get time slots within a specific time range
     */
    public List<TimeSlot> getTimeSlotsInTimeRange(LocalTime startTime, LocalTime endTime) {
        return getAllTimeSlots().stream()
            .filter(slot -> !slot.getStartTime().isBefore(startTime) && !slot.getEndTime().isAfter(endTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Check if a specific time slot is available
     */
    public boolean isTimeSlotAvailable(String courseId, String lecturerId, DayOfWeek dayOfWeek, 
                                       LocalTime startTime, LocalTime endTime) {
        // Get existing assignments for this lecturer
        List<ScheduledClass> lecturerAssignments = scheduledClassRepository.findByInstructorId(lecturerId);
        
        // Get existing assignments for this course
        List<ScheduledClass> courseAssignments = scheduledClassRepository.findByCourseId(courseId);
        
        // Create a time slot to check
        TimeSlot slotToCheck = new TimeSlot(dayOfWeek, startTime, endTime);
        
        // Check if the slot conflicts with any existing assignments
        return !conflictsWithExistingAssignments(slotToCheck, lecturerAssignments, courseAssignments);
    }
    
    /**
     * Get all time slots for a lecturer (occupied slots)
     */
    public List<TimeSlot> getTimeSlotsForLecturer(String lecturerId) {
        List<ScheduledClass> assignments = scheduledClassRepository.findByInstructorId(lecturerId);
        return assignmentsToTimeSlots(assignments);
    }
    
    /**
     * Get all time slots for a course (occupied slots)
     */
    public List<TimeSlot> getTimeSlotsForCourse(String courseId) {
        List<ScheduledClass> assignments = scheduledClassRepository.findByCourseId(courseId);
        return assignmentsToTimeSlots(assignments);
    }
    
    /**
     * Configure time slot parameters
     */
    public void configureTimeSlots(LocalTime startOfDay, LocalTime endOfDay, int slotDurationMinutes) {
        this.startOfDay = startOfDay;
        this.endOfDay = endOfDay;
        this.slotDurationMinutes = slotDurationMinutes;
    }
    
    /**
     * Convert assignments to time slots
     */
    private List<TimeSlot> assignmentsToTimeSlots(List<ScheduledClass> assignments) {
        return assignments.stream()
            .map(assignment -> new TimeSlot(
                assignment.getDayOfWeek(),
                assignment.getStartTime(),
                assignment.getEndTime()
            ))
            .collect(Collectors.toList());
    }
    
    /**
     * Check if a time slot conflicts with existing assignments
     */
    private boolean conflictsWithExistingAssignments(TimeSlot slot, 
                                                    List<ScheduledClass> lecturerAssignments, 
                                                    List<ScheduledClass> courseAssignments) {
        // Check if the time slot conflicts with any lecturer assignments
        boolean lecturerConflict = lecturerAssignments.stream().anyMatch(assignment -> 
            assignment.getDayOfWeek() == slot.getDayOfWeek() &&
            timeRangesOverlap(
                assignment.getStartTime(), assignment.getEndTime(),
                slot.getStartTime(), slot.getEndTime()
            )
        );
        
        if (lecturerConflict) {
            return true;
        }
        
        // Check if the time slot conflicts with any course assignments
        boolean courseConflict = courseAssignments.stream().anyMatch(assignment -> 
            assignment.getDayOfWeek() == slot.getDayOfWeek() &&
            timeRangesOverlap(
                assignment.getStartTime(), assignment.getEndTime(),
                slot.getStartTime(), slot.getEndTime()
            )
        );
        
        return courseConflict;
    }
    
    /**
     * Check if two time ranges overlap
     */
    private boolean timeRangesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        // Two time ranges overlap if one starts before the other ends
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}