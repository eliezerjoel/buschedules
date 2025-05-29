
// package dev.eliezerjoelk.buschedules.service;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import dev.eliezerjoelk.buschedules.repository.CourseRepository;
// import java.time.Duration;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Random;
// import java.util.Set;

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
//        List<Instructor> lecturers = InstructorRepo.findAll();
//         List<TimeSlot> timeSlots = TimeSlotService.getAllTimeSlots(); // Your method to fetch slots
        
//         // Initialize and run GA
//         List<Timetable> population = initializePopulation(courses, lecturers, timeSlots);
//         Timetable bestTimetable = evolvePopulation(population, MAX_GENERATIONS);
        
//         return mapToScheduledClasses(bestTimetable);
//     }

//     // --- GA CORE LOGIC ---
//     private Timetable evolvePopulation(List<Timetable> population, int maxGenerations) {
//         for (int gen = 0; gen < maxGenerations; gen++) {
//             population.sort(Comparator.comparingInt(this::calculateFitness));
//             Timetable best = population.get(0);
            
//             if (calculateFitness(best) == 0) { // Perfect schedule found
//                 return best;
//             }
            
//             population = nextGeneration(population);
//         }
//         return population.get(0);
//     }

//     // FITNESS FUNCTION (now properly placed)
//     private int calculateFitness(Timetable timetable) {
//         int conflicts = 0;
//         Map<Instructor, Set<TimeSlot>> lecturerSchedule = new HashMap<>();
        
//         for (ScheduledClass sc : timetable.getClasses()) {
//             Instructor lecturer = sc.getInstructor();
//             TimeSlot slot = sc.getTimeSlot();
            
//             // Lecturer double-booking check
//             if (lecturerSchedule.computeIfAbsent(lecturer, k -> new HashSet<>()).contains(slot)) {
//                 conflicts++;
//             } else {
//                 lecturerSchedule.get(lecturer).add(slot);
//             }
            
//             // Add department clash checks here if needed
//         }
//         return conflicts;
//     }

//     // --- HELPER METHODS ---
//     private List<Timetable> initializePopulation(List<Course> courses, List<Instructor> lecturers, List<TimeSlot> timeSlots) {
//         List<Timetable> population = new ArrayList<>();
//         for (int i = 0; i < POPULATION_SIZE; i++) {
//             population.add(createRandomTimetable(courses, Instructors, timeSlots));
//         }
//         return population;
//     }

//     private Timetable createRandomTimetable(List<Course> courses, List<Lecturer> lecturers, List<TimeSlot> timeSlots) {
//         Timetable timetable = new Timetable();
//         Random rand = new Random();
        
//         for (Course course : courses) {
//             Lecturer lecturer = getRandomLecturerForCourse(lecturers, course);
//             TimeSlot slot = timeSlots.get(rand.nextInt(timeSlots.size()));
//             timetable.addClass(new ScheduledClass(course, lecturer, slot, null));
//         }
//         return timetable;
//     }
// }