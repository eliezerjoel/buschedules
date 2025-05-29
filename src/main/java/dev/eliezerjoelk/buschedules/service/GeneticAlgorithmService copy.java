
// package dev.eliezerjoelk.buschedules.service;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import java.time.Duration;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.model.Course;
// import dev.eliezerjoelk.buschedules.model.Instructor;
// import dev.eliezerjoelk.buschedules.model.ScheduledClass;
// import dev.eliezerjoelk.buschedules.model.TimeSlot;
// import dev.eliezerjoelk.buschedules.model.Timetable;
// import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;
// import dev.eliezerjoelk.buschedules.repository.InstructorRepository;

// @Service
// public class GeneticAlgorithmService {
//     @Autowired
//     private CourseRepository courseRepo;
//     @Autowired
//     private InstructorRepository InstructorRepo;

//     private static final int POPULATION_SIZE = 50;
//     private static final double MUTATION_RATE = 0.1;
//     private static final int MAX_GENERATIONS = 100;

//     public List<ScheduledClass> generateSchedule() {
//         List<Course> courses = courseRepo.findAll();
//         List<Instructor> lecturers = InstructorRepo.findAll();
//         List<TimeSlot> timeSlots = Arrays.asList(
//             new TimeSlot("08:00-10:00"),
//             new TimeSlot("10:00-12:00"),
//             // Add all slots...
//         );

//         // Run GA (similar to standalone Java example)
//         Timetable bestTimetable = runGA(courses, lecturers, timeSlots);

//         // Convert to ScheduledClass entities
//         return mapToScheduledClasses(bestTimetable);
//     }

//     private List<ScheduledClass> mapToScheduledClasses(Timetable timetable) {
//         List<ScheduledClass> scheduledClasses = new ArrayList<>();
//         for (Course course : timetable.getAssignedCourses()) {
//             ScheduledClass sc = new ScheduledClass();
//             sc.setCourse(course);
//             sc.setInstructor(timetable.getInstructor(course));
//             sc.setTimeSlot(timetable.getTimeSlot(course));
//             // Room is left NULL (manual assignment later)
//             scheduledClasses.add(sc);
//         }
//         return scheduledClasses;
//     }
// }