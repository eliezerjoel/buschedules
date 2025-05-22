package dev.eliezerjoelk.buschedules.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.Assignment;
import dev.eliezerjoelk.buschedules.model.ScheduleAssignmentRequest;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.AssignmentService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    
    /**
     * Get all assignments
     */
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
    
    /**
     * Get assignment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable String id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * Create a new assignment
     */
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }
    
    /**
     * Update an existing assignment
     */
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        Optional<Assignment> updatedAssignment = assignmentService.updateAssignment(id, assignment);
        return updatedAssignment.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * Delete an assignment
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable String id) {
        assignmentService.deleteAssignment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Get assignments by course ID
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable String courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
    
    /**
     * Get assignments by instructor ID
     */
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByInstructor(@PathVariable String instructorId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByInstructor(instructorId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
    
    /**
     * Get assignments by student group ID
     */
    @GetMapping("/student-group/{studentGroupId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByStudentGroup(@PathVariable String studentGroupId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByStudentGroup(studentGroupId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
    
    /**
     * Get available time slots for a course-instructor combination
     */
    @GetMapping("/timeslots/available")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @RequestParam String courseId, 
            @RequestParam String instructorId) {
        List<TimeSlot> availableTimeSlots = assignmentService.getAvailableTimeSlots(courseId, instructorId);
        return new ResponseEntity<>(availableTimeSlots, HttpStatus.OK);
    }
    
    /**
     * Create a new assignment using the schedule assignment request
     */
    @PostMapping("/assign")
    public ResponseEntity<Assignment> assignCourse(@RequestBody ScheduleAssignmentRequest request) {
        try {
            Assignment assignment = assignmentService.createAssignment(
                request.getCourseId(),
                request.getInstructorId(),
                request.getStudentGroupId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime()
            );
            return new ResponseEntity<>(assignment, HttpStatus.CREATED);
        } catch (Exception e) {
            // In a real application, you might want to handle different exceptions differently
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}