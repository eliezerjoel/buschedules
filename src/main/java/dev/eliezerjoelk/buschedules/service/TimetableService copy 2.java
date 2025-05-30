// package dev.eliezerjoelk.buschedules.service;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.exception.ConflictException;
// import dev.eliezerjoelk.buschedules.exception.ResourceNotFoundException;
// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.model.ScheduledClass;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
// import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

// @Service
// public class TimetableService {
    
//     @Autowired
//     private CourseRepository courseRepository;
    
//     @Autowired
//     private InstructorRepository instructorRepository;
    
//     @Autowired
//     private ScheduledClassRepository scheduledClassRepository;
    
//     public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
//         // Get existing assignments for this instructor
//         List<ScheduledClass> instructorAssignments = scheduledClassRepository.findByInstructorId(instructorId);

//         // Get existing assignments for this course
//         List<ScheduledClass> courseAssignments = scheduledClassRepository.findByCourseId(courseId);
        
//         // Generate all possible time slots (e.g., Monday-Friday, 8AM-6PM, in 1-hour blocks)
//         List<TimeSlot> allTimeSlots = generateAllTimeSlots();
        
//         // Remove time slots that conflict with existing assignments
//         List<TimeSlot> availableTimeSlots = removeConflictingTimeSlots(allTimeSlots, 
//                                                                         instructorAssignments, 
//                                                                         courseAssignments);
        
//         return availableTimeSlots;
//     }
    
//     public ScheduledClass createAssignment(String courseId, String instructorId, 
//                                        TimeSlot timeSlot) {
//         Course course = courseRepository.findById(courseId)
//             .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
//         Instructor instructor = instructorRepository.findById(instructorId)
//             .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        
//         // Check if the time slot is still available
//         boolean isAvailable = checkTimeSlotAvailability(courseId, instructorId, timeSlot);
//         if (!isAvailable) {
//             throw new ConflictException("The selected time slot is no longer available");
//         }
        
//         // Create and save the new assignment
//         ScheduledClass scheduledClass = new ScheduledClass();
//         scheduledClass.setCourse(course);
//         scheduledClass.setInstructor(instructor);
//         scheduledClass.settimeSlot(timeSlot);



        
//         // Create and save the new assignment
//         // ScheduledClass scheduledClass = new ScheduledClass();
//         // scheduledClass.setCourse(course);
//         // scheduledClass.setInstructor(instructor);
//         // scheduledClass.setDayOfWeek(dayOfWeek);
//         // scheduledClass.setStartTime(startTime);
//         // scheduledClass.setEndTime(endTime);
        
//         return scheduledClassRepository.save(scheduledClass);
//     }
    
//     private List<TimeSlot> generateAllTimeSlots() {
//         List<TimeSlot> timeSlots = new ArrayList<>();
        
//         // Define business hours - from 8AM to 6PM
//         LocalTime startHour = LocalTime.of(8, 0);
//         LocalTime endHour = LocalTime.of(18, 0);
        
//         // Define duration of each slot (1 hour in this case)
//         int durationHours = 1;
        
//         // For each day Monday through Friday
//         for (DayOfWeek day : new DayOfWeek[] {
//                 DayOfWeek.MONDAY, 
//                 DayOfWeek.TUESDAY, 
//                 DayOfWeek.WEDNESDAY, 
//                 DayOfWeek.THURSDAY, 
//                 DayOfWeek.FRIDAY
//         }) {
//             // For each hour in the business day
//             for (int hour = startHour.getHour(); hour < endHour.getHour(); hour++) {
//                 LocalTime slotStart = LocalTime.of(hour, 0);
//                 LocalTime slotEnd = slotStart.plusHours(durationHours);
                
//                 TimeSlot timeSlot = new TimeSlot();
//                 timeSlot.setDayOfWeek(day);
//                 timeSlot.setStartTime(slotStart);
//                 timeSlot.setEndTime(slotEnd);
                
//                 timeSlots.add(timeSlot);
//             }
//         }
        
//         return timeSlots;
//     }
    
//     private List<TimeSlot> removeConflictingTimeSlots(List<TimeSlot> allTimeSlots, 
//                                                       List<ScheduledClass> instructorAssignments,
//                                                       List<ScheduledClass> courseAssignments) {
//         return allTimeSlots.stream()
//                 .filter(timeSlot -> !conflictsWithAnyAssignment(timeSlot, instructorAssignments))
//                 .filter(timeSlot -> !conflictsWithAnyAssignment(timeSlot, courseAssignments))
//                 .collect(Collectors.toList());
//     }
    
//     private boolean conflictsWithAnyAssignment(TimeSlot timeSlot, List<ScheduledClass> assignments) {
//         return assignments.stream().anyMatch(assignment -> 
//             assignment.getDayOfWeek() == timeSlot.getDayOfWeek() && 
//             // Check if the time ranges overlap
//             (
//                 // Assignment starts during the timeSlot
//                 (assignment.getStartTime().compareTo(timeSlot.getStartTime()) >= 0 && 
//                  assignment.getStartTime().compareTo(timeSlot.getEndTime()) < 0)
                 
//                  ||
                 
//                 // Assignment ends during the timeSlot
//                 (assignment.getEndTime().compareTo(timeSlot.getStartTime()) > 0 && 
//                  assignment.getEndTime().compareTo(timeSlot.getEndTime()) <= 0)
                 
//                  ||
                 
//                 // Assignment completely contains the timeSlot
//                 (assignment.getStartTime().compareTo(timeSlot.getStartTime()) <= 0 && 
//                  assignment.getEndTime().compareTo(timeSlot.getEndTime()) >= 0)
//             )
//         );
//     }
    
//     private boolean checkTimeSlotAvailability(String courseId, String instructorId, 
//                                              TimeSlot timeSlot) {
//         Create a timeSlot object from the parameters
//         TimeSlot requestedSlot = new TimeSlot();
//         requestedSlot.setDayOfWeek(timeSlot.getDayOfWeek());
//         requestedSlot.setStartTime(timeSlot.getStartTime());
//         requestedSlot.setEndTime(timeSlot.getEndTime());

//         // Get existing assignments for this instructor
//         List<ScheduledClass> instructorAssignments = scheduledClassRepository.findByInstructorId(instructorId);
        
//         // Get existing assignments for this course
//         List<ScheduledClass> courseAssignments = scheduledClassRepository.findByCourseId(courseId);
        
//         // Check if the requested time slot conflicts with any existing assignment
//         boolean instructorHasConflict = conflictsWithAnyAssignment(timeSlot, instructorAssignments);
//         boolean courseHasConflict = conflictsWithAnyAssignment(timeSlot, courseAssignments);
        
//         // The time slot is available if there are no conflicts
//         return !instructorHasConflict && !courseHasConflict;
//     }
// }