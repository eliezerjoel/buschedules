// package dev.eliezerjoelk.buschedules.controller;

// import java.time.DayOfWeek;
// import java.time.LocalTime;
// import java.util.List;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.service.TimeSlotService;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/timeslots")
// public class TimeSlotController {

//     @Autowired
//     private TimeSlotService timeSlotService;

//     /**
//      * Get all available time slots
//      */
//     @GetMapping
//     public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
//         return ResponseEntity.ok(timeSlotService.getAllTimeSlots());
//     }

//     /**
//      * Get available time slots for a specific course and lecturer
//      */
//     @GetMapping("/available")
//     public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
//             @RequestParam String courseId,
//             @RequestParam String instructorId) {
//         return ResponseEntity.ok(timeSlotService.getAvailableTimeSlots(courseId, instructorId));
//     }

//     /**
//      * Get available time slots for a specific day
//      */
//     @GetMapping("/by-day")
//     public ResponseEntity<List<TimeSlot>> getTimeSlotsForDay(
//             @RequestParam DayOfWeek dayOfWeek) {
//         return ResponseEntity.ok(timeSlotService.getTimeSlotsForDay(dayOfWeek));
//     }

//     /**
//      * Get available time slots within a time range
//      */
//     @GetMapping("/by-time-range")
//     public ResponseEntity<List<TimeSlot>> getTimeSlotsInTimeRange(
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
//         return ResponseEntity.ok(timeSlotService.getTimeSlotsInTimeRange(startTime, endTime));
//     }

//     /**
//      * Check if a particular time slot is available
//      */
//     @GetMapping("/check-availability")
//     public ResponseEntity<Map<String, Boolean>> checkTimeSlotAvailability(
//             @RequestParam String courseId,
//             @RequestParam String instructorId,
//             @RequestParam DayOfWeek dayOfWeek,
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        
//         boolean isAvailable = timeSlotService.isTimeSlotAvailable(
//             courseId, instructorId, dayOfWeek, startTime, endTime);
        
//         return ResponseEntity.ok(Map.of("available", isAvailable));
//     }

//     /**
//      * Get all time slots for a lecturer
//      */
//     @GetMapping("/lecturer/{instructorId}")
//     public ResponseEntity<List<TimeSlot>> getTimeSlotsForLecturer(
//             @PathVariable String instructorId) {
//         return ResponseEntity.ok(timeSlotService.getTimeSlotsForLecturer(instructorId));
//     }

//     /**
//      * Get all time slots for a course
//      */
//     @GetMapping("/course/{courseId}")
//     public ResponseEntity<List<TimeSlot>> getTimeSlotsForCourse(
//             @PathVariable String courseId) {
//         return ResponseEntity.ok(timeSlotService.getTimeSlotsForCourse(courseId));
//     }

//     /**
//      * Configure time slot parameters (admin only)
//      */
//     @PostMapping("/configure")
//     public ResponseEntity<Map<String, Object>> configureTimeSlots(
//             @RequestBody TimeSlotConfigRequest config) {
//         timeSlotService.configureTimeSlots(
//             config.getStartOfDay(), 
//             config.getEndOfDay(),
//             config.getSlotDurationMinutes());
        
//         return ResponseEntity.ok(Map.of(
//             "message", "Time slot configuration updated successfully",
//             "startOfDay", config.getStartOfDay(),
//             "endOfDay", config.getEndOfDay(),
//             "slotDurationMinutes", config.getSlotDurationMinutes()
//         ));
//     }

//     /**
//      * Request object for time slot configuration
//      */
//     public static class TimeSlotConfigRequest {
//         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//         private LocalTime startOfDay;
        
//         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//         private LocalTime endOfDay;
        
//         private int slotDurationMinutes;

//         // Getters and setters
//         public LocalTime getStartOfDay() {
//             return startOfDay;
//         }

//         public void setStartOfDay(LocalTime startOfDay) {
//             this.startOfDay = startOfDay;
//         }

//         public LocalTime getEndOfDay() {
//             return endOfDay;
//         }

//         public void setEndOfDay(LocalTime endOfDay) {
//             this.endOfDay = endOfDay;
//         }

//         public int getSlotDurationMinutes() {
//             return slotDurationMinutes;
//         }

//         public void setSlotDurationMinutes(int slotDurationMinutes) {
//             this.slotDurationMinutes = slotDurationMinutes;
//         }
//     }
// }