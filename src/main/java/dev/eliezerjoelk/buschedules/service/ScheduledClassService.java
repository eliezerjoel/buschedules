package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime; // Import Duration
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // Import DateTimeFormatter
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.exception.SchedulingConflictException;
import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot; // Import StudentGroup
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;

@Service
public class ScheduledClassService {

    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository; // Autowire StudentGroupRepository

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Helper method to check if two time intervals overlap
    private boolean doTimesOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        LocalTime start1 = startTime1;
        LocalTime end1 = endTime1;
        LocalTime start2 = startTime2;
        LocalTime end2 = endTime2;

        // An overlap occurs if the intervals (start1, end1) and (start2, end2) intersect.
        // This is true if start1 is before end2 AND end1 is after start2.
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    public List<ScheduledClass> getAllScheduledClasses() {
        return scheduledClassRepository.findAll();
    }

    public Optional<ScheduledClass> getScheduledClassById(String id) {
        return scheduledClassRepository.findById(id);
    }

    // Original createScheduledClass method, now with conflict detection
    public ScheduledClass createScheduledClass(ScheduledClass scheduledClass) throws SchedulingConflictException {
        // Check for instructor conflict
        List<ScheduledClass> instructorSchedules = scheduledClassRepository.findByInstructorAndDayOfWeek(scheduledClass.getInstructor(), scheduledClass.getDayOfWeek());
        for (ScheduledClass existingSchedule : instructorSchedules) {
            // Ensure we're not checking against itself if it's an update (though this is for creation)
            // If ID exists and matches, it's an update and should be handled by updateScheduledClass
            if (scheduledClass.getId() == null || !scheduledClass.getId().equals(existingSchedule.getId())) {
                if (doTimesOverlap(scheduledClass.getStartTime(), scheduledClass.getEndTime(), existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                    throw new SchedulingConflictException(
                            String.format("Instructor %s is already scheduled for another class at this time on %s.",
                                          scheduledClass.getInstructor().getFirstName() + " " + scheduledClass.getInstructor().getLastName(),
                                          scheduledClass.getDayOfWeek()));
                }
            }
        }

        // Check for student group conflict
        List<ScheduledClass> studentGroupSchedules = scheduledClassRepository.findByStudentGroupAndDayOfWeek(scheduledClass.getStudentGroup(), scheduledClass.getDayOfWeek());
        for (ScheduledClass existingSchedule : studentGroupSchedules) {
            // Ensure we're not checking against itself if it's an update
            if (scheduledClass.getId() == null || !scheduledClass.getId().equals(existingSchedule.getId())) {
                if (doTimesOverlap(scheduledClass.getStartTime(), scheduledClass.getEndTime(), existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                    throw new SchedulingConflictException(
                            String.format("Student group %s is already scheduled for another class at this time on %s.",
                                          scheduledClass.getStudentGroup().getGroupName(),
                                          scheduledClass.getDayOfWeek()));
                }
            }
        }

        return scheduledClassRepository.save(scheduledClass);
    }

    // Original updateScheduledClass method, now with conflict detection
    public Optional<ScheduledClass> updateScheduledClass(String id, ScheduledClass updatedClass) throws SchedulingConflictException {
        return scheduledClassRepository.findById(id)
                .map(existingClass -> {
                    // Check for instructor conflict (excluding the class being updated)
                    List<ScheduledClass> instructorSchedules = scheduledClassRepository.findByInstructorAndDayOfWeek(updatedClass.getInstructor(), updatedClass.getDayOfWeek());
                    for (ScheduledClass otherSchedule : instructorSchedules) {
                        if (!otherSchedule.getId().equals(id) &&
                            doTimesOverlap(updatedClass.getStartTime(), updatedClass.getEndTime(), otherSchedule.getStartTime(), otherSchedule.getEndTime())) {
                            throw new SchedulingConflictException(
                                    String.format("Instructor %s is already scheduled for another class at this time on %s.",
                                                  updatedClass.getInstructor().getFirstName() + " " + updatedClass.getInstructor().getLastName(),
                                                  updatedClass.getDayOfWeek()));
                        }
                    }

                    // Check for student group conflict (excluding the class being updated)
                    List<ScheduledClass> studentGroupSchedules = scheduledClassRepository.findByStudentGroupAndDayOfWeek(updatedClass.getStudentGroup(), updatedClass.getDayOfWeek());
                    for (ScheduledClass otherSchedule : studentGroupSchedules) {
                        if (!otherSchedule.getId().equals(id) &&
                            doTimesOverlap(updatedClass.getStartTime(), updatedClass.getEndTime(), otherSchedule.getStartTime(), otherSchedule.getEndTime())) {
                            throw new SchedulingConflictException(
                                    String.format("Student group %s is already scheduled for another class at this time on %s.",
                                                  updatedClass.getStudentGroup().getGroupName(),
                                                  updatedClass.getDayOfWeek()));
                        }
                    }

                    // Update fields from updatedClass
                    existingClass.setCourse(updatedClass.getCourse());
                    existingClass.setInstructor(updatedClass.getInstructor());
                    existingClass.setDayOfWeek(updatedClass.getDayOfWeek());
                    existingClass.setStartTime(updatedClass.getStartTime());
                    existingClass.setEndTime(updatedClass.getEndTime());
                    existingClass.setStudentGroup(updatedClass.getStudentGroup());
                    existingClass.setSemester(updatedClass.getSemester()); // Assuming semester is part of updatedClass
                    existingClass.setAcademicYear(updatedClass.getAcademicYear()); // Assuming academicYear is part of updatedClass
                    
                    return scheduledClassRepository.save(existingClass);
                });
    }

    public void deleteScheduledClass(String id) {
        scheduledClassRepository.deleteById(id);
    }
    
    /**
     * Implement the method called by the controller for individual conflict checks.
     * This method will use existing conflict detection logic.
     */
    public boolean hasScheduleConflict(String lecturerId, DayOfWeek dayOfWeek,LocalTime startTime,LocalTime endTime) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(lecturerId);
        if (instructorOptional.isEmpty()) {
            // If instructor not found, no conflict for them in this check scenario.
            // Or you might want to throw an IllegalArgumentException if instructor must exist.
            return false;
        }
        Instructor instructor = instructorOptional.get();

        // Check for instructor conflict
        List<ScheduledClass> instructorSchedules = scheduledClassRepository.findByInstructorAndDayOfWeek(instructor, dayOfWeek);
        for (ScheduledClass existingSchedule : instructorSchedules) {
            if (doTimesOverlap(startTime, endTime, existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                return true; // Conflict found for instructor
            }
        }

        // IMPORTANT: If you want to check for StudentGroup conflicts here too,
        // you would need to pass studentGroupId to this method as well from the frontend.
        // For now, this method only checks instructor conflicts as per controller's current parameters.
        // If the frontend also sends a studentGroupId, you would fetch it and add a similar loop:
        /*
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isPresent()) {
            StudentGroup studentGroup = studentGroupOptional.get();
            List<ScheduledClass> studentGroupSchedules = scheduledClassRepository.findByStudentGroupAndDayOfWeek(studentGroup, dayOfWeek);
            for (ScheduledClass existingSchedule : studentGroupSchedules) {
                if (doTimesOverlap(startTime, endTime, existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                    return true; // Conflict found for student group
                }
            }
        }
        */

        return false; // No conflict found
    }

    /**
     * Get available time slots for a course-instructor combination
     *
     * IMPORTANT: This method's logic for `findByInstructorId` and `hasConflict` needs to be adapted.
     * `findByInstructorId` is not standard for repository and hasConflict requires TimeSlot object.
     * We'll adapt it to use existing repository methods.
     */
    public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
        // Generate all possible time slots
        List<TimeSlot> allTimeSlots = generateAllPossibleTimeSlots();
        
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            // Handle case where instructor is not found
            return new ArrayList<>(); // Or throw an exception
        }
        Instructor instructor = instructorOptional.get();

        // Get the instructor's already scheduled classes using the correct repository method
        List<ScheduledClass> instructorScheduledClasses = scheduledClassRepository.findByInstructorAndDayOfWeek(instructor, null); // Pass null for DayOfWeek for all days, or iterate through days.
        // A more robust implementation might fetch all schedule classes for instructor regardless of day and filter by day later.
        // For accurate available slots, you should fetch all scheduled classes for the instructor across all days.
        List<ScheduledClass> allInstructorSchedules = scheduledClassRepository.findByInstructor(instructor); // This method should be added to repo

        // Filter out time slots that conflict with the instructor's schedule
        List<TimeSlot> availableTimeSlots = allTimeSlots.stream()
            .filter(timeSlot -> !hasConflict(timeSlot, allInstructorSchedules))
            .collect(Collectors.toList());
        
        return availableTimeSlots;
    }
    
    /**
     * Create a scheduled class with the given parameters, adapted for the /assign endpoint.
     * This method will now throw SchedulingConflictException.
     */
    public ScheduledClass createScheduledClass(String courseId, String instructorId, String studentGroupId, // Added studentGroupId
                                             DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
                                             String semester, String academicYear) throws SchedulingConflictException { // Added semester & academicYear
        // Find the course, instructor, and student group
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Instructor instructor = instructorRepository.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));

        StudentGroup studentGroup = studentGroupRepository.findById(studentGroupId)
            .orElseThrow(() -> new RuntimeException("Student Group not found with id: " + studentGroupId));
        
        // Create a temporary ScheduledClass object for conflict checking
        ScheduledClass newScheduledClass = new ScheduledClass();
        newScheduledClass.setInstructor(instructor);
        newScheduledClass.setStudentGroup(studentGroup);
        newScheduledClass.setDayOfWeek(dayOfWeek);
        newScheduledClass.setStartTime(startTime);
        newScheduledClass.setEndTime(endTime);
        
        // Use the existing createScheduledClass logic to perform conflict checks
        // This will throw SchedulingConflictException if a conflict is found
        return createScheduledClass(newScheduledClass); // This calls the overloaded method with conflict detection
    }
    
    /**
     * Generate all possible time slots for scheduling
     * This remains the same as previously defined.
     */
    private List<TimeSlot> generateAllPossibleTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        // Create time slots for each day and hour based on your school's schedule
        // Example: Monday-Friday, 8 AM to 5 PM in 1-hour blocks
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}; // Changed to String for consistency
        
        for (String day : days) {
            for (int hour = 8; hour < 17; hour++) {
                LocalTime startTime = LocalTime.of(hour, 0);
                LocalTime endTime = LocalTime.of(hour + 1, 0);
                // TimeSlot constructor needs to be adapted if it takes DayOfWeek enum
                // Assuming TimeSlot can also handle String for DayOfWeek for flexibility or conversion within it
                timeSlots.add(new TimeSlot(DayOfWeek.valueOf(day.toUpperCase()), startTime, endTime)); // Assuming TimeSlot takes DayOfWeek enum
            }
        }
        return timeSlots;
    }
    
    /**
     * Check if a time slot conflicts with existing schedule.
     * This method needs to handle string dayOfWeek and string times if used with current repo methods.
     */
    private boolean hasConflict(TimeSlot timeSlot, List<ScheduledClass> schedule) {
        return schedule.stream().anyMatch(sc -> 
            sc.getDayOfWeek().equals(timeSlot.getDayOfWeek().toString()) && // Compare string dayOfWeek
            (doTimesOverlap(timeSlot.getStartTime(), timeSlot.getEndTime(), sc.getStartTime(), sc.getEndTime()))
        );
    }
}