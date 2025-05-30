package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;
    private ScheduledClassRepository scheduledClassRepository;

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(String id) {
        return instructorRepository.findById(id);
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public Optional<Instructor> updateInstructor(String id, Instructor updatedInstructor) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    instructor.setFirstName(updatedInstructor.getFirstName());
                    instructor.setLastName(updatedInstructor.getLastName());
                    instructor.setEmail(updatedInstructor.getEmail());
                    instructor.setDepartment(updatedInstructor.getDepartment());
                    return instructorRepository.save(instructor);
                });
    }

    public void deleteInstructor(String id) {
        instructorRepository.deleteById(id);
    }
    
    public List<Instructor> getAvailableInstructorsForCourse() {
        return instructorRepository.findAll();
    }
//     public int calculateWeeklyWorkload(String instructorId) {
//     // Get all assignments for this instructor
//     List<ScheduledClass> scheduledClass = scheduledClassRepository.findByInstructorId(instructorId);
//     System.out.println("scheduledClasses: " + scheduledClass);
//     // Calculate total hours per week
//     return scheduledClass.stream()
//         .mapToInt(assignment -> {
//             // Calculate duration in hours for each assignment
//             LocalTime start = (assignment.getStartTime());
//             LocalTime end = (assignment.getEndTime());
//             return (int) Duration.between(start, end).toHours();
//         })
//         .sum();
        
// }
    
}





//     public int calculateWeeklyWorkload(String instructorId) {
//     // Get all assignments for this instructor
//     List<Assignment> assignments = assignmentRepository.findByInstructorId(instructorId);
    
//     // Calculate total hours per week
//     return assignments.stream()
//         .mapToInt(assignment -> {
//             // Calculate duration in hours for each assignment
//             LocalTime start = LocalTime.parse(assignment.getStartTime());
//             LocalTime end = LocalTime.parse(assignment.getEndTime());
//             return (int) Duration.between(start, end).toHours();
//         })
//         .sum();
// }