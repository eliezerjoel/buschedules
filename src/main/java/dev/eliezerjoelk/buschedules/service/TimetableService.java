package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.exception.ConflictException;
import dev.eliezerjoelk.buschedules.exception.ResourceNotFoundException;
import dev.eliezerjoelk.buschedules.model.Assignment;
import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.repository.AssignmentRepository;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;

@Service
public class TimetableService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
        // Get existing assignments for this instructor
        List<Assignment> instructorAssignments = assignmentRepository.findByInstructorId(instructorId);
        
        // Get existing assignments for this course
        List<Assignment> courseAssignments = assignmentRepository.findByCourseId(courseId);
        
        // Generate all possible time slots (e.g., Monday-Friday, 8AM-6PM, in 1-hour blocks)
        List<TimeSlot> allTimeSlots = generateAllTimeSlots();
        
        // Remove time slots that conflict with existing assignments
        List<TimeSlot> availableTimeSlots = removeConflictingTimeSlots(allTimeSlots, 
                                                                        instructorAssignments, 
                                                                        courseAssignments);
        
        return availableTimeSlots;
    }
    
    public Assignment createAssignment(String courseId, String instructorId, 
                                       DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        Instructor instructor = instructorRepository.findById(instructorId)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        
        // Check if the time slot is still available
        boolean isAvailable = checkTimeSlotAvailability(courseId, instructorId, dayOfWeek, startTime, endTime);
        if (!isAvailable) {
            throw new ConflictException("The selected time slot is no longer available");
        }
        
        // Create and save the new assignment
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setInstructor(instructor);
        assignment.setDayOfWeek(dayOfWeek);
        assignment.setStartTime(startTime);
        assignment.setEndTime(endTime);
        
        return assignmentRepository.save(assignment);
    }
    
    private List<TimeSlot> generateAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        
        // Define business hours - from 8AM to 6PM
        LocalTime startHour = LocalTime.of(8, 0);
        LocalTime endHour = LocalTime.of(18, 0);
        
        // Define duration of each slot (1 hour in this case)
        int durationHours = 1;
        
        // For each day Monday through Friday
        for (DayOfWeek day : new DayOfWeek[] {
                DayOfWeek.MONDAY, 
                DayOfWeek.TUESDAY, 
                DayOfWeek.WEDNESDAY, 
                DayOfWeek.THURSDAY, 
                DayOfWeek.FRIDAY
        }) {
            // For each hour in the business day
            for (int hour = startHour.getHour(); hour < endHour.getHour(); hour++) {
                LocalTime slotStart = LocalTime.of(hour, 0);
                LocalTime slotEnd = slotStart.plusHours(durationHours);
                
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setDayOfWeek(day);
                timeSlot.setStartTime(slotStart);
                timeSlot.setEndTime(slotEnd);
                
                timeSlots.add(timeSlot);
            }
        }
        
        return timeSlots;
    }
    
    private List<TimeSlot> removeConflictingTimeSlots(List<TimeSlot> allTimeSlots, 
                                                      List<Assignment> instructorAssignments,
                                                      List<Assignment> courseAssignments) {
        return allTimeSlots.stream()
                .filter(timeSlot -> !conflictsWithAnyAssignment(timeSlot, instructorAssignments))
                .filter(timeSlot -> !conflictsWithAnyAssignment(timeSlot, courseAssignments))
                .collect(Collectors.toList());
    }
    
    private boolean conflictsWithAnyAssignment(TimeSlot timeSlot, List<Assignment> assignments) {
        return assignments.stream().anyMatch(assignment -> 
            assignment.getDayOfWeek() == timeSlot.getDayOfWeek() && 
            // Check if the time ranges overlap
            (
                // Assignment starts during the timeSlot
                (assignment.getStartTime().compareTo(timeSlot.getStartTime()) >= 0 && 
                 assignment.getStartTime().compareTo(timeSlot.getEndTime()) < 0)
                 
                 ||
                 
                // Assignment ends during the timeSlot
                (assignment.getEndTime().compareTo(timeSlot.getStartTime()) > 0 && 
                 assignment.getEndTime().compareTo(timeSlot.getEndTime()) <= 0)
                 
                 ||
                 
                // Assignment completely contains the timeSlot
                (assignment.getStartTime().compareTo(timeSlot.getStartTime()) <= 0 && 
                 assignment.getEndTime().compareTo(timeSlot.getEndTime()) >= 0)
            )
        );
    }
    
    private boolean checkTimeSlotAvailability(String courseId, String instructorId, 
                                             DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        // Create a timeSlot object from the parameters
        TimeSlot requestedSlot = new TimeSlot();
        requestedSlot.setDayOfWeek(dayOfWeek);
        requestedSlot.setStartTime(startTime);
        requestedSlot.setEndTime(endTime);
        
        // Get existing assignments for this instructor
        List<Assignment> instructorAssignments = assignmentRepository.findByInstructorId(instructorId);
        
        // Get existing assignments for this course
        List<Assignment> courseAssignments = assignmentRepository.findByCourseId(courseId);
        
        // Check if the requested time slot conflicts with any existing assignment
        boolean instructorHasConflict = conflictsWithAnyAssignment(requestedSlot, instructorAssignments);
        boolean courseHasConflict = conflictsWithAnyAssignment(requestedSlot, courseAssignments);
        
        // The time slot is available if there are no conflicts
        return !instructorHasConflict && !courseHasConflict;
    }
}