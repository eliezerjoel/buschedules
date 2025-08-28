// package dev.eliezerjoelk.buschedules.controller;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;

// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.ScheduledClass;
// import dev.eliezerjoelk.buschedules.model.Timetable;
// import dev.eliezerjoelk.buschedules.repository.DepartmentRepository;
// import dev.eliezerjoelk.buschedules.service.TimetableService;
// import dev.eliezerjoelk.buschedules.dto.DepartmentTimetable;
// import dev.eliezerjoelk.buschedules.dto.ScheduledClassDto;
// import dev.eliezerjoelk.buschedules.dto.ScheduledClassDto;
// import dev.eliezerjoelk.buschedules.dto.TimetableRequest;
// import dev.eliezerjoelk.buschedules.dto.TimetableResponse;
// // import dev.eliezerjoelk.buschedules.dto.TimetableStatistics;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
// import org.slf4j.LoggerFactory;

// @RestController
// @RequestMapping("/api/timetable")
// @CrossOrigin(origins = "*")
// public class TimetableController {

//     @Autowired
//     private TimetableService timetableService;
//     @Autowired
// private DepartmentRepository departmentRepository;

// @PostMapping("/generate")
// public ResponseEntity<TimetableResponse> generateTimetable() {
//     try {
//         Timetable generatedTimetable = timetableService.generateTimetable();

//         // Group ScheduledClass by departmentId and extract unique courses
//         Map<String, List<Course>> coursesByDepartment = generatedTimetable.getClasses().stream()
//             .collect(Collectors.groupingBy(
//                 sc -> sc.getCourse().getDepartmentId(),
//                 Collectors.mapping(
//                     sc -> sc.getCourse(),
//                     Collectors.collectingAndThen(
//                         Collectors.toSet(),
//                         ArrayList::new
//                     )
//                 )
//             ));

//         List<DepartmentTimetable> departments = coursesByDepartment.entrySet().stream()
//             .map(entry -> {
//                 String deptId = entry.getKey();
//                 List<Course> courses = entry.getValue();
//                 // Fetch the department name from the repository
//                 String deptName = departmentRepository.findById(deptId)
//                     .map(department -> department.getName())
//                     .orElse(deptId); // Fallback to department ID if not found

//                 // Convert courses to ScheduledClassDto list for this department
//                 List<ScheduledClassDto> scheduledClassDtos = generatedTimetable.getClasses().stream()
//                     .filter(sc -> sc.getCourse().getDepartmentId().equals(deptId))
//                     .map(sc -> new ScheduledClassDto(sc)) // Assuming ScheduledClassDto has a constructor that takes ScheduledClass
//                     .collect(Collectors.toList());

//                 return new DepartmentTimetable(deptId, deptName, scheduledClassDtos);
//             })
//             .collect(Collectors.toList());

//         TimetableResponse response = new TimetableResponse(
//             departments,
//             "Timetable generated successfully",
//             true
//         );
//         return ResponseEntity.ok(response);
//     } catch (Exception e) {
//         TimetableResponse errorResponse = new TimetableResponse(
//             null,
//             "Failed to generate timetable: " + e.getMessage(),
//             false
//         );
//     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// }

// @GetMapping
// public ResponseEntity<TimetableResponse> getCurrentTimetable() {
//     Timetable timetable = timetableService.getCurrentTimetable();
//     if (timetable == null) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//             .body(new TimetableResponse(null, "No timetable found", false));
//     }

//     // Group ScheduledClass objects by departmentId
//     Map<String, List<ScheduledClass>> classesByDepartment = timetable.getClasses().stream()
//         .collect(Collectors.groupingBy(sc -> sc.getCourse().getDepartmentId()));

//     List<DepartmentTimetable> departments = classesByDepartment.entrySet().stream()
//         .map(entry -> {
//             String deptId = entry.getKey();
//             List<ScheduledClass> classes = entry.getValue();

//             // Fetch the department name from the repository
//             String deptName = departmentRepository.findById(deptId)
//                 .map(department -> department.getName())
//                 .orElse(deptId); // Fallback to department ID if not found

//             // Map ScheduledClass objects to DTOs
//             List<ScheduledClassDto> classesDto = classes.stream()
//                 .map(sc -> new ScheduledClassDto(
//                     sc.getId(),
//                     sc.getCourse().getCourseCode(),
//                     sc.getCourse().getCourseName(),
//                     sc.getInstructor().getLastName(),
//                     sc.getInstructor().getFirstName(),
//                     sc.getTimeSlot().getDayOfWeek(),
//                     sc.getTimeSlot().getStartTime(),
//                     sc.getTimeSlot().getEndTime(),
//                     sc.getCourse().getDepartmentId()
//                 ))
//                 .collect(Collectors.toList());

//             return new DepartmentTimetable(deptId, deptName, classesDto);
//         })
//         .collect(Collectors.toList());

//     return ResponseEntity.ok(new TimetableResponse(departments, "Timetable retrieved successfully", true));
// }
//     // @GetMapping
//     // public ResponseEntity<TimetableResponse> getCurrentTimetable() {
//     //     Timetable timetable = timetableService.getCurrentTimetable();
//     //     if (timetable == null) {
//     //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
//     //             .body(new TimetableResponse(null, "No timetable found", false));
//     //     }
//     //     return ResponseEntity.ok(new TimetableResponse(timetable, "Timetable retrieved successfully", true));
//     // }

// // (Removed misplaced DepartmentTimetable class definition. It should be in its own file.)
    
// }




// // @GetMapping
// // public ResponseEntity<TimetableResponse> getCurrentTimetable() {
// //     Timetable timetable = timetableService.getCurrentTimetable();
// //     if (timetable == null) {
// //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
// //             .body(new TimetableResponse(null, "No timetable found", false));
// //     }

// //     // Group ScheduledClass by departmentId and extract unique courses
// //     Map<String, List<Course>> coursesByDepartment = timetable.getClasses().stream()
// //         .collect(Collectors.groupingBy(
// //             sc -> sc.getCourse().getDepartmentId(),
// //             Collectors.mapping(
// //                 sc -> sc.getCourse(),
// //                 Collectors.collectingAndThen(
// //                     Collectors.toSet(),
// //                     ArrayList::new
// //                 )
// //             )
// //         ));

// //     List<DepartmentTimetable> departments = coursesByDepartment.entrySet().stream()
// //     .map(entry -> {
// //         String deptId = entry.getKey();
// //         List<Course> courses = entry.getValue();
// //         // Fetch the department name from the repository
// //         String deptName = departmentRepository.findById(deptId)
// //             .map(department -> department.getName())
// //             .orElse(deptId); // Fallback to department ID if not found
// //         return new DepartmentTimetable(deptId, deptName, courses);
// //     })

// //     .collect(Collectors.toList());
// //     return ResponseEntity.ok(new TimetableResponse(departments, "Timetable retrieved successfully", true));
// // }
//     @DeleteMapping
//     public ResponseEntity<String> deleteCurrentTimetable() {
//         timetableService.deleteCurrentTimetable();
//         return ResponseEntity.ok("Timetable deleted successfully");
//     }
// }