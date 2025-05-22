// package dev.eliezerjoelk.buschedules.controller;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import dev.eliezerjoelk.buschedules.model.Assignment;
// import dev.eliezerjoelk.buschedules.model.ScheduleAssignmentRequest;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.service.AssignmentService;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/assignments")
// public class AssignmentController {

  
    
  
//     /**
//      * Get assignments by course ID
//      */
//     @GetMapping("/course/{courseId}")
//     public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable String courseId) {
//         List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
//         return new ResponseEntity<>(assignments, HttpStatus.OK);
//     }
    
//     /**
//      * Get assignments by instructor ID
//      */
//     @GetMapping("/instructor/{instructorId}")
//     public ResponseEntity<List<Assignment>> getAssignmentsByInstructor(@PathVariable String instructorId) {
//         List<Assignment> assignments = assignmentService.getAssignmentsByInstructor(instructorId);
//         return new ResponseEntity<>(assignments, HttpStatus.OK);
//     }
    
//     /**
//      * Get assignments by student group ID
//      */
//     @GetMapping("/student-group/{studentGroupId}")
//     public ResponseEntity<List<Assignment>> getAssignmentsByStudentGroup(@PathVariable String studentGroupId) {
//         List<Assignment> assignments = assignmentService.getAssignmentsByStudentGroup(studentGroupId);
//         return new ResponseEntity<>(assignments, HttpStatus.OK);
//     }
    
// }