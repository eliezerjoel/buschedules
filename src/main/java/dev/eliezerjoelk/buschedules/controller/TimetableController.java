package dev.eliezerjoelk.buschedules.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.service.TimetableService;
import dev.eliezerjoelk.buschedules.dto.DepartmentTimetable;
import dev.eliezerjoelk.buschedules.dto.TimetableRequest;
import dev.eliezerjoelk.buschedules.dto.TimetableResponse;
// import dev.eliezerjoelk.buschedules.dto.TimetableStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;
@PostMapping("/generate")
public ResponseEntity<TimetableResponse> generateTimetable() {
    try {
        Timetable generatedTimetable = timetableService.generateTimetable();

        // Group ScheduledClass by departmentId and extract unique courses
        Map<String, List<Course>> coursesByDepartment = generatedTimetable.getClasses().stream()
            .collect(Collectors.groupingBy(
                sc -> sc.getCourse().getDepartmentId(),
                Collectors.mapping(
                    sc -> sc.getCourse(),
                    Collectors.collectingAndThen(
                        Collectors.toSet(),
                        ArrayList::new
                    )
                )
            ));

        List<DepartmentTimetable> departments = coursesByDepartment.entrySet().stream()
            .map(entry -> new DepartmentTimetable(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

        TimetableResponse response = new TimetableResponse(
            departments,
            "Timetable generated successfully",
            true
        );
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        TimetableResponse errorResponse = new TimetableResponse(
            null,
            "Failed to generate timetable: " + e.getMessage(),
            false
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
    // @GetMapping
    // public ResponseEntity<TimetableResponse> getCurrentTimetable() {
    //     Timetable timetable = timetableService.getCurrentTimetable();
    //     if (timetable == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //             .body(new TimetableResponse(null, "No timetable found", false));
    //     }
    //     return ResponseEntity.ok(new TimetableResponse(timetable, "Timetable retrieved successfully", true));
    // }
@GetMapping
public ResponseEntity<TimetableResponse> getCurrentTimetable() {
    Timetable timetable = timetableService.getCurrentTimetable();
    if (timetable == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new TimetableResponse(null, "No timetable found", false));
    }

    // Group ScheduledClass by departmentId and extract unique courses
    Map<String, List<Course>> coursesByDepartment = timetable.getClasses().stream()
        .collect(Collectors.groupingBy(
            sc -> sc.getCourse().getDepartmentId(),
            Collectors.mapping(
                sc -> sc.getCourse(),
                Collectors.collectingAndThen(
                    Collectors.toSet(),
                    ArrayList::new
                )
            )
        ));

    List<DepartmentTimetable> departments = coursesByDepartment.entrySet().stream()
        .map(entry -> new DepartmentTimetable(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(new TimetableResponse(departments, "Timetable retrieved successfully", true));
}
    @DeleteMapping
    public ResponseEntity<String> deleteCurrentTimetable() {
        timetableService.deleteCurrentTimetable();
        return ResponseEntity.ok("Timetable deleted successfully");
    }
}