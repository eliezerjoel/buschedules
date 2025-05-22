package dev.eliezerjoelk.buschedules.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import dev.eliezerjoelk.buschedules.dto.ConflictCheckRequest;
import dev.eliezerjoelk.buschedules.exception.SchedulingConflictException;
import dev.eliezerjoelk.buschedules.model.ScheduleAssignmentRequest;
import dev.eliezerjoelk.buschedules.model.ScheduledClass; // Assuming TimeSlot is a valid model
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.ScheduledClassService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/scheduled-classes")
public class ScheduledClassController {

    @Autowired
    private ScheduledClassService scheduledClassService;

    @GetMapping
    public ResponseEntity<List<ScheduledClass>> getAllScheduledClasses() {
        List<ScheduledClass> scheduledClasses = scheduledClassService.getAllScheduledClasses();
        return new ResponseEntity<>(scheduledClasses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledClass> getScheduledClassById(@PathVariable String id) {
        Optional<ScheduledClass> scheduledClass = scheduledClassService.getScheduledClassById(id);
        return scheduledClass.map(sc -> new ResponseEntity<>(sc, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createScheduledClass(@RequestBody ScheduledClass scheduledClass) {
        try {
            ScheduledClass createdClass = scheduledClassService.createScheduledClass(scheduledClass);
            return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
        } catch (SchedulingConflictException e) {
            // Handle the specific conflict exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            // Handle any other unexpected errors
            return new ResponseEntity<>("Error creating schedule: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkScheduledClasses(@RequestBody List<ScheduledClass> scheduledClasses) {
        try {
            List<ScheduledClass> createdClasses = new java.util.ArrayList<>();
            for (ScheduledClass scheduledClass : scheduledClasses) {
                // Each creation can throw a conflict exception
                createdClasses.add(scheduledClassService.createScheduledClass(scheduledClass));
            }
            return new ResponseEntity<>(createdClasses, HttpStatus.CREATED);
        } catch (SchedulingConflictException e) {
            // If any single scheduled class causes a conflict, return 409 for the whole bulk operation
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating multiple schedules: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScheduledClass(@PathVariable String id,
            @RequestBody ScheduledClass updatedClass) {
        try {
            Optional<ScheduledClass> updated = scheduledClassService.updateScheduledClass(id, updatedClass);
            return updated.map(sc -> new ResponseEntity<>(sc, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (SchedulingConflictException e) {
            // Handle the specific conflict exception for updates
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            // Handle any other unexpected errors
            return new ResponseEntity<>("Error updating schedule: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledClass(@PathVariable String id) {
        scheduledClassService.deleteScheduledClass(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get available time slots for course-instructor combination
    @GetMapping("/timeslots/available")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @RequestParam String courseId,
            @RequestParam String instructorId) {
        // This method assumes scheduledClassService has getAvailableTimeSlots defined
        return ResponseEntity.ok(scheduledClassService.getAvailableTimeSlots(courseId, instructorId));
    }
@PostMapping("/check-conflict")
public ResponseEntity<?> checkScheduleConflict(@RequestBody ConflictCheckRequest request) {
    try {
        DayOfWeek parsedDayOfWeek = DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase());
        LocalTime parsedStartTime = LocalTime.parse(request.getStartTime());
        LocalTime parsedEndTime = LocalTime.parse(request.getEndTime());

        boolean hasConflict = scheduledClassService.hasScheduleConflict(
                request.getLecturerId(),
                parsedDayOfWeek,
                parsedStartTime,
                parsedEndTime
        );
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasConflict", hasConflict);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(
            Map.of("error", "Invalid input: " + e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }
}
    // Save assignment
    @PostMapping("/assign")
    public ResponseEntity<?> assignCourse( // Changed return type to wildcard for error handling
            @RequestBody ScheduleAssignmentRequest request) {
        try {
            // Ensure createScheduledClass can handle these parameters or map them to a ScheduledClass object
            ScheduledClass scheduledClass = scheduledClassService.createScheduledClass(
                    request.getCourseId(),
                    request.getInstructorId(),
                    request.getStudentGroupId(), // Assuming ScheduleAssignmentRequest has studentGroupId
                    request.getDayOfWeek(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getSemester(), // Assuming ScheduleAssignmentRequest has semester
                    request.getAcademicYear() // Assuming ScheduleAssignmentRequest has academicYear
            );
            return ResponseEntity.ok(scheduledClass);
        } catch (SchedulingConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error assigning course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}






    // @PostMapping("/check-conflict") // Changed to use @RequestParam
    // public ResponseEntity<Boolean> checkScheduleConflict(
    //         @RequestParam String lecturerId,
    //         @RequestParam DayOfWeek dayOfWeek,
    //         @RequestParam LocalTime startTime,
    //         @RequestParam LocalTime endTime) {
    //     boolean hasConflict = scheduledClassService.hasScheduleConflict(
    //             lecturerId,
    //             dayOfWeek,
    //             startTime,
    //             endTime
    //     );
    //     return new ResponseEntity<>(hasConflict, HttpStatus.OK);
    // }
