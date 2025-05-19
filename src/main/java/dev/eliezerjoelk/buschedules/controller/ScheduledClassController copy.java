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

// import dev.eliezerjoelk.buschedules.model.ScheduleAssignmentRequest;
// import dev.eliezerjoelk.buschedules.model.ScheduledClass;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.service.ScheduledClassService;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/scheduled-classes")
// public class ScheduledClassController {

//     @Autowired
//     private ScheduledClassService scheduledClassService;

//     @GetMapping
//     public ResponseEntity<List<ScheduledClass>> getAllScheduledClasses() {
//         List<ScheduledClass> scheduledClasses = scheduledClassService.getAllScheduledClasses();
//         return new ResponseEntity<>(scheduledClasses, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ScheduledClass> getScheduledClassById(@PathVariable String id) {
//         Optional<ScheduledClass> scheduledClass = scheduledClassService.getScheduledClassById(id);
//         return scheduledClass.map(sc -> new ResponseEntity<>(sc, HttpStatus.OK))
//                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//     }

//     @PostMapping
//     public ResponseEntity<ScheduledClass> createScheduledClass(@RequestBody ScheduledClass scheduledClass) {
//         ScheduledClass createdClass = scheduledClassService.createScheduledClass(scheduledClass);
//         return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
//     }

//     @PostMapping("/bulk")
//     public ResponseEntity<?> createBulkScheduledClasses(@RequestBody List<ScheduledClass> scheduledClasses) {
//         try {
//             List<ScheduledClass> createdClasses = new java.util.ArrayList<>();
//             for (ScheduledClass scheduledClass : scheduledClasses) {
//                 createdClasses.add(scheduledClassService.createScheduledClass(scheduledClass));
//             }
//             return new ResponseEntity<>(createdClasses, HttpStatus.CREATED);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Error creating multiple schedules: " + e.getMessage(),
//                     HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ScheduledClass> updateScheduledClass(@PathVariable String id,
//             @RequestBody ScheduledClass updatedClass) {
//         Optional<ScheduledClass> updated = scheduledClassService.updateScheduledClass(id, updatedClass);
//         return updated.map(sc -> new ResponseEntity<>(sc, HttpStatus.OK))
//                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteScheduledClass(@PathVariable String id) {
//         scheduledClassService.deleteScheduledClass(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     // Get available time slots for course-instructor combination
// @GetMapping("/timeslots/available")
// public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
//         @RequestParam String courseId, 
//         @RequestParam String instructorId) {
//     return ResponseEntity.ok(scheduledClassService.getAvailableTimeSlots(courseId, instructorId));
// }

// @PostMapping("/check-conflict")
//     public ResponseEntity<Boolean> checkScheduleConflict(@RequestBody ConflictCheckDTO checkRequest) {
//         boolean hasConflict = scheduleService.hasScheduleConflict(
//             checkRequest.getLecturerId(),
//             checkRequest.getDay(),
//             checkRequest.getStartTime(),
//             checkRequest.getEndTime()
//         );
//         return new ResponseEntity<>(hasConflict, HttpStatus.OK);
//     }
// // Save assignment
// @PostMapping("/assign")
// public ResponseEntity<ScheduledClass> assignCourse(
//         @RequestBody ScheduleAssignmentRequest request) {
//     ScheduledClass scheduledClass = scheduledClassService.createScheduledClass(
//         request.getCourseId(), 
//         request.getInstructorId(),
//         request.getDayOfWeek(),
//         request.getStartTime(),
//         request.getEndTime()
//     );
//     return ResponseEntity.ok(scheduledClass);
// }
// }