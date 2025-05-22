// package dev.eliezerjoelk.buschedules.service;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.model.Assignment;
// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.model.StudentGroup;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.repository.AssignmentRepository;
// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
// import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;

// @Service
// public class AssignmentService {

//     @Autowired
//     private AssignmentRepository assignmentRepository;
    
//     @Autowired
//     private CourseRepository courseRepository;
    
//     @Autowired
//     private InstructorRepository instructorRepository;
    
//     @Autowired
//     private StudentGroupRepository studentGroupRepository;
    
//     /**
//      * Get all assignments
//      */
//     public List<Assignment> getAllAssignments() {
//         return assignmentRepository.findAll();
//     }
    
//     /**
//      * Get assignment by ID
//      */
//     public Optional<Assignment> getAssignmentById(String id) {
//         return assignmentRepository.findById(id);
//     }
    
//     /**
//      * Create a new assignment
//      */
//     public Assignment createAssignment(Assignment assignment) {
//         return assignmentRepository.save(assignment);
//     }
    
//     /**
//      * Update an existing assignment
//      */
//     public Optional<Assignment> updateAssignment(String id, Assignment updatedAssignment) {
//         return assignmentRepository.findById(id)
//                 .map(existingAssignment -> {
//                     existingAssignment.setCourse(updatedAssignment.getCourse());
//                     existingAssignment.setInstructor(updatedAssignment.getInstructor());
//                     existingAssignment.setStudentGroup(updatedAssignment.getStudentGroup());
//                     existingAssignment.setDayOfWeek(updatedAssignment.getDayOfWeek());
//                     existingAssignment.setStartTime(updatedAssignment.getStartTime());
//                     existingAssignment.setEndTime(updatedAssignment.getEndTime());
//                     return assignmentRepository.save(existingAssignment);
//                 });
//     }
    
//     /**
//      * Delete an assignment
//      */
//     public void deleteAssignment(String id) {
//         assignmentRepository.deleteById(id);
//     }
    
//     /**
//      * Get assignments by course ID
//      */
//     public List<Assignment> getAssignmentsByCourse(String courseId) {
//         return assignmentRepository.findByCourseId(courseId);
//     }
    
//     /**
//      * Get assignments by instructor ID
//      */
//     public List<Assignment> getAssignmentsByInstructor(String instructorId) {
//         return assignmentRepository.findByInstructorId(instructorId);
//     }
    
//     /**
//      * Get assignments by student group ID
//      */
//     public List<Assignment> getAssignmentsByStudentGroup(String studentGroupId) {
//         return assignmentRepository.findByStudentGroupId(studentGroupId);
//     }
    
//     /**
//      * Get available time slots for a course-instructor combination
//      */
//     public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
//         // Get all possible time slots
//         List<TimeSlot> allTimeSlots = generateAllPossibleTimeSlots();
        
//         // Get existing assignments for this instructor
//         List<Assignment> instructorAssignments = assignmentRepository.findByInstructorId(instructorId);
        
//         // Get existing assignments for this course
//         List<Assignment> courseAssignments = assignmentRepository.findByCourseId(courseId);
        
//         // Filter out time slots that conflict with instructor's schedule
//         List<TimeSlot> availableTimeSlots = allTimeSlots.stream()
//                 .filter(timeSlot -> !hasConflict(timeSlot, instructorAssignments))
//                 .filter(timeSlot -> !hasConflict(timeSlot, courseAssignments))
//                 .collect(Collectors.toList());
        
//         return availableTimeSlots;
//     }
    
//     /**
//      * Create a new assignment with the given parameters
//      */
//     public Assignment createAssignment(String courseId, String instructorId, String studentGroupId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
//         // Find the course, instructor, and student group
//         Course course = courseRepository.findById(courseId)
//                 .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
//         Instructor instructor = instructorRepository.findById(instructorId)
//                 .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));
        
//         StudentGroup studentGroup = null;
//         if (studentGroupId != null && !studentGroupId.isEmpty()) {
//             studentGroup = studentGroupRepository.findById(studentGroupId)
//                     .orElseThrow(() -> new RuntimeException("Student Group not found with id: " + studentGroupId));
//         }
        
//         // Check if the time slot is available
//         List<Assignment> instructorAssignments = assignmentRepository.findByInstructorId(instructorId);
//         List<Assignment> courseAssignments = assignmentRepository.findByCourseId(courseId);
        
//         TimeSlot requestedTimeSlot = new TimeSlot(dayOfWeek, startTime, endTime);
        
//         if (hasConflict(requestedTimeSlot, instructorAssignments)) {
//             throw new IllegalStateException("The selected time slot conflicts with the instructor's schedule");
//         }
        
//         if (hasConflict(requestedTimeSlot, courseAssignments)) {
//             throw new IllegalStateException("The selected time slot conflicts with the course's schedule");
//         }
        
//         // Create and save the assignment
//         Assignment assignment = new Assignment();
//         assignment.setCourse(course);
//         assignment.setInstructor(instructor);
//         assignment.setStudentGroup(studentGroup);
//         assignment.setDayOfWeek(dayOfWeek);
//         assignment.setStartTime(startTime);
//         assignment.setEndTime(endTime);
        
//         return assignmentRepository.save(assignment);
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
//      * Check if a time slot conflicts with existing assignments
//      */
//     private boolean hasConflict(TimeSlot timeSlot, List<Assignment> assignments) {
//         return assignments.stream().anyMatch(assignment -> 
//             assignment.getDayOfWeek() == timeSlot.getDayOfWeek() && 
//             (timeSlot.getStartTime().isBefore(assignment.getEndTime()) && 
//              timeSlot.getEndTime().isAfter(assignment.getStartTime()))
//         );
//     }
// }