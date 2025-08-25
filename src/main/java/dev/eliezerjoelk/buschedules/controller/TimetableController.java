package dev.eliezerjoelk.buschedules.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.service.TimetableService;
import dev.eliezerjoelk.buschedules.dto.TimetableRequest;
import dev.eliezerjoelk.buschedules.dto.TimetableResponse;
// import dev.eliezerjoelk.buschedules.dto.TimetableStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*") // Configure as needed for your frontend
public class TimetableController {
    
    @Autowired
    private TimetableService timetableService;
    
    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);
    
    @PostMapping("/generate")
    public ResponseEntity<TimetableResponse> generateTimetable(@RequestBody TimetableRequest request) {
        try {
            logger.info("Received request to generate timetable for semester: {}, year: {}", 
                       request.getSemester(), request.getAcademicYear());
            
            Timetable generatedTimetable = timetableService.generateTimetable(
                request.getSemester(), request.getAcademicYear());
            
            TimetableResponse response = new TimetableResponse(
                generatedTimetable, 
                "Timetable generated successfully", 
                true
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating timetable", e);
            
            TimetableResponse errorResponse = new TimetableResponse(
                null, 
                "Failed to generate timetable: " + e.getMessage(), 
                false
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // @GetMapping("/{semester}/{academicYear}")
    // public ResponseEntity<TimetableResponse> getTimetable(
    //         @PathVariable String semester, 
    //         @PathVariable String academicYear) {
    //     try {
    //         Timetable timetable = timetableService.getTimetable(semester, academicYear);
            
    //         TimetableResponse response = new TimetableResponse(
    //             timetable, 
    //             "Timetable retrieved successfully", 
    //             true
    //         );
            
    //         return ResponseEntity.ok(response);
            
    //     } catch (Exception e) {
    //         logger.error("Error retrieving timetable for semester: {}, year: {}", semester, academicYear, e);
            
    //         TimetableResponse errorResponse = new TimetableResponse(
    //             null, 
    //             "Timetable not found: " + e.getMessage(), 
    //             false
    //         );
            
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    //     }
    // }
    
    @GetMapping("/all")
    public ResponseEntity<List<Timetable>> getAllTimetables() {
        try {
            List<Timetable> timetables = timetableService.getAllTimetables();
            return ResponseEntity.ok(timetables);
            
        } catch (Exception e) {
            logger.error("Error retrieving all timetables", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // @DeleteMapping("/{semester}/{academicYear}")
    // public ResponseEntity<String> deleteTimetable(
    //         @PathVariable String semester, 
    //         @PathVariable String academicYear) {
    //     try {
    //         timetableService.deleteTimetable(semester, academicYear);
    //         return ResponseEntity.ok("Timetable deleted successfully");
            
    //     } catch (Exception e) {
    //         logger.error("Error deleting timetable for semester: {}, year: {}", semester, academicYear, e);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body("Failed to delete timetable: " + e.getMessage());
    //     }
    // }
    
    // @GetMapping("/statistics/{semester}/{academicYear}")
    // public ResponseEntity<TimetableStatistics> getTimetableStatistics(
    //         @PathVariable String semester, 
    //         @PathVariable String academicYear) {
    //     try {
    //         TimetableStatistics stats = timetableService.getTimetableStatistics(semester, academicYear);
    //         return ResponseEntity.ok(stats);
            
    //     } catch (Exception e) {
    //         logger.error("Error retrieving timetable statistics", e);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
}