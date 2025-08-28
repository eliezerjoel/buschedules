package dev.eliezerjoelk.buschedules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.repository.DepartmentRepository;
import dev.eliezerjoelk.buschedules.service.TimetableService;
import dev.eliezerjoelk.buschedules.dto.DepartmentTimetable;
import dev.eliezerjoelk.buschedules.dto.ScheduledClassDto;
import dev.eliezerjoelk.buschedules.dto.TimetableResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;
    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/generate")
    public ResponseEntity<TimetableResponse> generateTimetable() {
        try {
            Timetable generatedTimetable = timetableService.generateTimetable();

            Map<String, List<ScheduledClass>> classesByDepartment = generatedTimetable.getClasses().stream()
                    .collect(Collectors.groupingBy(sc -> sc.getCourse().getDepartmentId()));

            List<DepartmentTimetable> departments = classesByDepartment.entrySet().stream()
                    .map(entry -> {
                        String deptId = entry.getKey();
                        List<ScheduledClass> classes = entry.getValue();
                        String deptName = departmentRepository.findById(deptId)
                                .map(department -> department.getName())
                                .orElse(deptId);

                        List<ScheduledClassDto> classesDto = classes.stream()
                                .map(sc -> new ScheduledClassDto(
                                        sc.getId(),
                                        sc.getCourse().getCourseCode(),
                                        sc.getCourse().getCourseName(),
                                        sc.getInstructor().getFirstName() + " " + sc.getInstructor().getLastName(),
                                        sc.getCourse().getDepartmentId(),
                                        sc.getTimeSlot().getDayOfWeek(),
                                        sc.getTimeSlot().getStartTime(),
                                        sc.getTimeSlot().getEndTime()))
                                .collect(Collectors.toList());

                        return new DepartmentTimetable(deptId, deptName, classesDto);
                    })
                    .collect(Collectors.toList());

            TimetableResponse response = new TimetableResponse(
                    departments,
                    "Timetable generated successfully",
                    true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            TimetableResponse errorResponse = new TimetableResponse(
                    null,
                    "Failed to generate timetable: " + e.getMessage(),
                    false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<TimetableResponse> getCurrentTimetable() {
        Timetable timetable = timetableService.getCurrentTimetable();
        if (timetable == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TimetableResponse(null, "No timetable found", false));
        }

        Map<String, List<ScheduledClass>> classesByDepartment = timetable.getClasses().stream()
                .collect(Collectors.groupingBy(sc -> sc.getCourse().getDepartmentId()));

        List<DepartmentTimetable> departments = classesByDepartment.entrySet().stream()
                .map(entry -> {
                    String deptId = entry.getKey();
                    List<ScheduledClass> classes = entry.getValue();

                    String deptName = departmentRepository.findById(deptId)
                            .map(department -> department.getName())
                            .orElse(deptId);

                    List<ScheduledClassDto> classesDto = classes.stream()
                            .map(sc -> new ScheduledClassDto(
                                    sc.getId(),
                                    sc.getCourse().getCourseCode(),
                                    sc.getCourse().getCourseName(),
                                    sc.getInstructor().getFirstName() + " " + sc.getInstructor().getLastName(),
                                    sc.getCourse().getDepartmentId(),
                                    sc.getTimeSlot().getDayOfWeek(),
                                    sc.getTimeSlot().getStartTime(),
                                    sc.getTimeSlot().getEndTime()))
                            .collect(Collectors.toList());

                    return new DepartmentTimetable(deptId, deptName, classesDto);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new TimetableResponse(departments, "Timetable retrieved successfully", true));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCurrentTimetable() {
        timetableService.deleteCurrentTimetable();
        return ResponseEntity.ok("Timetable deleted successfully");
    }
}