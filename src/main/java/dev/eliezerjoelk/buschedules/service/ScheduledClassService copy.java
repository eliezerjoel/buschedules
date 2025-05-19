// package dev.eliezerjoelk.buschedules.service;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.model.ScheduledClass;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
// import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

// @Service
// public class ScheduledClassService {

//     @Autowired
//     private ScheduledClassRepository scheduledClassRepository;
    
//     @Autowired
//     private CourseRepository courseRepository;
    
//     @Autowired
//     private InstructorRepository instructorRepository;

//     public List<ScheduledClass> getAllScheduledClasses() {
//         return scheduledClassRepository.findAll();
//     }

//     public Optional<ScheduledClass> getScheduledClassById(String id) {
//         return scheduledClassRepository.findById(id);
//     }

//     public ScheduledClass createScheduledClass(ScheduledClass scheduledClass) {
//         return scheduledClassRepository.save(scheduledClass);
//     }

//     public Optional<ScheduledClass> updateScheduledClass(String id, ScheduledClass updatedClass) {
//         return scheduledClassRepository.findById(id)
//                 .map(existingClass -> {
//                     // Update fields from updatedClass
//                     existingClass.setCourse(updatedClass.getCourse());
//                     existingClass.setInstructor(updatedClass.getInstructor());
//                     existingClass.setDayOfWeek(updatedClass.getDayOfWeek());
//                     existingClass.setStartTime(updatedClass.getStartTime());
//                     existingClass.setEndTime(updatedClass.getEndTime());
//                     existingClass.setStudentGroup(updatedClass.getStudentGroup());
//                     // Save and return the updated entity
//                     return scheduledClassRepository.save(existingClass);
//                 });
//     }

//     public void deleteScheduledClass(String id) {
//         scheduledClassRepository.deleteById(id);
//     }
    
//     /**
//      * Get available time slots for a course-instructor combination
//      */
//     public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
//         // Generate all possible time slots
//         List<TimeSlot> allTimeSlots = generateAllPossibleTimeSlots();
        
//         // Get the instructor's already scheduled classes
//         List<ScheduledClass> instructorSchedule = scheduledClassRepository.findByInstructorId(instructorId);
        
//         // Filter out time slots that conflict with the instructor's schedule
//         List<TimeSlot> availableTimeSlots = allTimeSlots.stream()
//             .filter(timeSlot -> !hasConflict(timeSlot, instructorSchedule))
//             .collect(Collectors.toList());
        
//         return availableTimeSlots;
//     }
    
//     /**
//      * Create a scheduled class with the given parameters
//      */
//     public ScheduledClass createScheduledClass(String courseId, String instructorId, 
//                                              DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
//         // Find the course and instructor
//         Course course = courseRepository.findById(courseId)
//             .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
//         Instructor instructor = instructorRepository.findById(instructorId)
//             .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));
        
//         // Check if the time slot is available
//         List<ScheduledClass> existingSchedule = scheduledClassRepository.findByInstructorId(instructorId);
//         TimeSlot requestedTimeSlot = new TimeSlot(dayOfWeek, startTime, endTime);
        
//         if (hasConflict(requestedTimeSlot, existingSchedule)) {
//             throw new IllegalStateException("The selected time slot conflicts with the instructor's schedule");
//         }
        
//         // Create and save the scheduled class
//         ScheduledClass scheduledClass = new ScheduledClass();
//         scheduledClass.setCourse(course);
//         scheduledClass.setInstructor(instructor);
//         scheduledClass.setDayOfWeek(dayOfWeek);
//         scheduledClass.setStartTime(startTime);
//         scheduledClass.setEndTime(endTime);
//         // Other fields like room might need to be set based on your requirements
        
//         return scheduledClassRepository.save(scheduledClass);
//     }
    
//     /**
//      * Generate all possible time slots for scheduling
//      */
//     private List<TimeSlot> generateAllPossibleTimeSlots() {
//         List<TimeSlot> timeSlots = new ArrayList<>();
//         // Create time slots for each day and hour based on your school's schedule
//         // Example: Monday-Friday, 8 AM to 5 PM in 1-hour blocks
//         DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
//                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        
//         for (DayOfWeek day : days) {
//             for (int hour = 8; hour < 17; hour++) {
//                 LocalTime startTime = LocalTime.of(hour, 0);
//                 LocalTime endTime = LocalTime.of(hour + 1, 0);
//                 timeSlots.add(new TimeSlot(day, startTime, endTime));
//             }
//         }
//         return timeSlots;
//     }
    
//     /**
//      * Check if a time slot conflicts with existing schedule
//      */
//     private boolean hasConflict(TimeSlot timeSlot, List<ScheduledClass> schedule) {
//         return schedule.stream().anyMatch(sc -> 
//             sc.getDayOfWeek() == timeSlot.getDayOfWeek() && 
//             (timeSlot.getStartTime().isBefore(sc.getEndTime()) && 
//              timeSlot.getEndTime().isAfter(sc.getStartTime()))
//         );
//     }
// }