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
// import org.springframework.web.bind.annotation.RestController;

// import dev.eliezerjoelk.buschedules.dto.InstructorDto;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.service.InstructorService;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/instructors")
// public class InstructorController {

//     @Autowired
//     private InstructorService instructorService;

//     @GetMapping
//     public ResponseEntity<List<Instructor>> getAllInstructors() {
//         List<Instructor> instructors = instructorService.getAllInstructors();
//         return new ResponseEntity<>(instructors, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Instructor> getInstructorById(@PathVariable String id) {
//         Optional<Instructor> instructor = instructorService.getInstructorById(id);
//         return instructor.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
//                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//     }

//     @PostMapping
//     public ResponseEntity<Instructor> createInstructor(@RequestBody InstructorDto instructorDto) {
//         Instructor newInstructor = instructorService.createInstructor(instructorDto.getInstructor(),
//                 instructorDto.getCourseIds());
//         return ResponseEntity.status(HttpStatus.CREATED).body(newInstructor);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Instructor> updateInstructor(@PathVariable String id,
//             @RequestBody InstructorDto instructorDto) {
//         Optional<Instructor> updatedInstructor = instructorService.updateInstructor(id, instructorDto.getInstructor(),
//                 instructorDto.getCourseIds());
//         return updatedInstructor.map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteInstructor(@PathVariable String id) {
//         instructorService.deleteInstructor(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     @GetMapping("/{id}/workload")
//     public ResponseEntity<Integer> getInstructorWorkload(@PathVariable String id) {
//         try {
//             // Calculate total teaching hours per week
//             // int totalHours = instructorService.calculateWeeklyWorkload(id);

//             int totalHours = 8; // Placeholder for actual calculation
//             return new ResponseEntity<>(totalHours, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }

//     // Get available instructors for a course
//     // @GetMapping("/available")
//     // public ResponseEntity<List<Instructor>>
//     // getAvailableInstructorsForCourse(@RequestParam String courseId) {
//     // try {
//     // List<Instructor> availableInstructors =
//     // instructorService.getAvailableInstructorsForCourse(courseId);
//     // return new ResponseEntity<>(availableInstructors, HttpStatus.OK);
//     // } catch (Exception e) {
//     // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     // }
//     // }

// }