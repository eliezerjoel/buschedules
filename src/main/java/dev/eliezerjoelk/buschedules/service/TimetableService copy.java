// package dev.eliezerjoelk.buschedules.service;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.model.Assignment;
// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
// import dev.eliezerjoelk.buschedules.repository.AssignmentRepository;
// import dev.eliezerjoelk.buschedules.exception.ConflictException;
// import dev.eliezerjoelk.buschedules.exception.ResourceNotFoundException;

// @Service
// public class TimetableService {
    
//     @Autowired
//     private CourseRepository courseRepository;
    
//     @Autowired
//     private InstructorRepository instructorRepository;
    
//     @Autowired
//     private AssignmentRepository assignmentRepository;
    
//     public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
//         // Get existing assignments for this instructor
//         List<Assignment> instructorAssignments = assignmentRepository.findByInstructorId(instructorId);
        
//         // Get existing assignments for this course
//         List<Assignment> courseAssignments = assignmentRepository.findByCourseId(courseId);
        
//         // Generate all possible time slots (e.g., Monday-Friday, 8AM-6PM, in 1-hour blocks)
//         List<TimeSlot> allTimeSlots = generateAllTimeSlots();
        
//         // Remove time slots that conflict with existing assignments
//         List<TimeSlot> availableTimeSlots = removeConflictingTimeSlots(allTimeSlots, 
//                                                                         instructorAssignments, 
//                                                                         courseAssignments);
        
//         return availableTimeSlots;
//     }
    
//     public Assignment createAssignment(String courseId, String instructorId, 
//                                        DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
//         Course course = courseRepository.findById(courseId)
//             .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
//         Instructor instructor = instructorRepository.findById(instructorId)
//             .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        
//         // Check if the time slot is still available
//         boolean isAvailable = checkTimeSlotAvailability(courseId, instructorId, dayOfWeek, startTime, endTime);
//         if (!isAvailable) {
//             throw new ConflictException("The selected time slot is no longer available");
//         }
        
//         // Create and save the new assignment
//         Assignment assignment = new Assignment();
//         assignment.setCourse(course);
//         assignment.setInstructor(instructor);
//         assignment.setDayOfWeek(dayOfWeek);
//         assignment.setStartTime(startTime);
//         assignment.setEndTime(endTime);
        
//         return assignmentRepository.save(assignment);
//     }
    
//     private List<TimeSlot> generateAllTimeSlots() {
//         // Implementation to generate all possible time slots
//         // ...
//     }
    
//     private List<TimeSlot> removeConflictingTimeSlots(List<TimeSlot> allTimeSlots, 
//                                                       List<Assignment> instructorAssignments,
//                                                       List<Assignment> courseAssignments) {
//         // Implementation to filter out conflicting time slots
//         // ...
//     }
    
//     private boolean checkTimeSlotAvailability(String courseId, String instructorId, 
//                                              DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
//         // Implementation to check if the time slot is still available
//         // ...
//     }
// }