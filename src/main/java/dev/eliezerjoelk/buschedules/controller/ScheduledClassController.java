package dev.eliezerjoelk.buschedules.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.service.ScheduledClassService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/scheduled-classes")
public class ScheduledClassController {

    @Autowired
    private ScheduledClassService scheduledClassService;

    // Basic CRUD operations
    @GetMapping
    public ResponseEntity<List<ScheduledClass>> getAllScheduledClasses() {
        return ResponseEntity.ok(scheduledClassService.getAllScheduledClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledClass> getScheduledClassById(@PathVariable String id) {
        return scheduledClassService.getScheduledClassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledClass(@PathVariable String id) {
        scheduledClassService.deleteScheduledClass(id);
        return ResponseEntity.noContent().build();
    }

    // Genetic Algorithm endpoints
    @PostMapping("/generate")
    public ResponseEntity<List<ScheduledClass>> generateSchedule() {
        return ResponseEntity.ok(scheduledClassService.generateOptimalSchedule());
    }

    @PostMapping("/save-generated")
    public ResponseEntity<List<ScheduledClass>> saveGeneratedSchedule() {
        return ResponseEntity.ok(scheduledClassService.saveGeneratedSchedule());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAllSchedules() {
        scheduledClassService.clearAllSchedules();
        return ResponseEntity.noContent().build();
    }

    // Query endpoints
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ScheduledClass>> findByCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(scheduledClassService.findByCourse(courseId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<ScheduledClass>> findByInstructor(@PathVariable String instructorId) {
        return ResponseEntity.ok(scheduledClassService.findByInstructor(instructorId));
    }

    @GetMapping("/student-group/{studentGroupId}")
    public ResponseEntity<List<ScheduledClass>> findByStudentGroup(@PathVariable String studentGroupId) {
        return ResponseEntity.ok(scheduledClassService.findByStudentGroup(studentGroupId));
    }

    // Validation endpoint
    // @PostMapping("/validate")
    // public ResponseEntity<Map<String, Boolean>> validateSchedule(@RequestBody List<ScheduledClass> schedule) {
    //     return ResponseEntity.ok(Map.of(
    //         "isValid", scheduledClassService.validateSchedule(schedule)
    //     ));
    // }

    // Exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}