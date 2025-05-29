
package dev.eliezerjoelk.buschedules.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;


@Service
public class GeneticAlgorithmService {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private InstructorRepository InstructorRepo;

    private static final int POPULATION_SIZE = 50;
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_GENERATIONS = 100;
    public List<ScheduledClass> generateSchedule() {
        List<Course> courses = courseRepo.findAll();
        List<Instructor> lecturers = InstructorRepo.findAll();
        List<TimeSlot> timeSlots = getAllTimeSlots(); // Implement this method
        
        // Initialize and run GA
        List<Timetable> population = initializePopulation(courses, lecturers, timeSlots);
        Timetable bestTimetable = evolvePopulation(population, MAX_GENERATIONS);
        
        return mapToScheduledClasses(bestTimetable);
    }

    private List<TimeSlot> getAllTimeSlots() {
        // Implement based on your time slot management
        // Example:
        return Arrays.asList(
            new TimeSlot("08:00-10:00"),
            new TimeSlot("10:00-12:00"),
            new TimeSlot("13:00-15:00"),
            new TimeSlot("15:00-17:00")
        );
    }

    private List<ScheduledClass> mapToScheduledClasses(Timetable timetable) {
        return timetable.getClasses();
    }

    private Timetable evolvePopulation(List<Timetable> population, int maxGenerations) {
        for (int gen = 0; gen < maxGenerations; gen++) {
            population.sort(Comparator.comparingInt(this::calculateFitness));
            Timetable best = population.get(0);
            
            if (calculateFitness(best) == 0) { // Perfect schedule found
                return best;
            }
            
            population = nextGeneration(population);
        }
        return population.get(0);
    }

    private int calculateFitness(Timetable timetable) {
        int conflicts = 0;
        Map<Lecturer, Set<TimeSlot>> lecturerSchedule = new HashMap<>();
        
        for (ScheduledClass sc : timetable.getClasses()) {
            Instructor lecturer = sc.getLecturer();
            TimeSlot slot = sc.getTimeSlot();
            
            // Lecturer double-booking check
            if (lecturerSchedule.computeIfAbsent(lecturer, k -> new HashSet<>()).contains(slot)) {
                conflicts++;
            } else {
                lecturerSchedule.get(lecturer).add(slot);
            }
        }
        return conflicts;
    }

    private List<Timetable> initializePopulation(List<Course> courses, List<Instructor> lecturers, List<TimeSlot> timeSlots) {
        List<Timetable> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(createRandomTimetable(courses, lecturers, timeSlots));
        }
        return population;
    }

    private Timetable createRandomTimetable(List<Course> courses, List<Instructor> lecturers, List<TimeSlot> timeSlots) {
        Timetable timetable = new Timetable();
        Random rand = new Random();
        
        for (Course course : courses) {
            Instructor lecturer = getRandomLecturerForCourse(lecturers, course);
            TimeSlot slot = timeSlots.get(rand.nextInt(timeSlots.size()));
            timetable.addClass(new ScheduledClass(course, lecturer, slot, null));
        }
        return timetable;
    }

    private Instructor getRandomLecturerForCourse(List<Instructor> lecturers, Course course) {
        List<Instructor> possibleLecturers = lecturers.stream()
            .filter(l -> l.getCourses().contains(course))
            .collect(Collectors.toList());
        
        if (possibleLecturers.isEmpty()) {
            throw new IllegalStateException("No lecturer available for course: " + course.getCode());
        }
        
        return possibleLecturers.get(new Random().nextInt(possibleLecturers.size()));
    }

    private List<Timetable> nextGeneration(List<Timetable> population) {
        List<Timetable> newPopulation = new ArrayList<>();
        
        // Keep the best individual (elitism)
        population.sort(Comparator.comparingInt(this::calculateFitness));
        newPopulation.add(population.get(0));
        
        // Fill the rest of the population
        while (newPopulation.size() < POPULATION_SIZE) {
            Timetable parent1 = selectParent(population);
            Timetable parent2 = selectParent(population);
            Timetable child = crossover(parent1, parent2);
            mutate(child);
            newPopulation.add(child);
        }
        
        return newPopulation;
    }

    private Timetable selectParent(List<Timetable> population) {
        // Tournament selection
        Random rand = new Random();
        Timetable best = null;
        for (int i = 0; i < 3; i++) {
            Timetable candidate = population.get(rand.nextInt(population.size()));
            if (best == null || calculateFitness(candidate) < calculateFitness(best)) {
                best = candidate;
            }
        }
        return best;
    }

    private Timetable crossover(Timetable parent1, Timetable parent2) {
        // Single-point crossover
        Timetable child = new Timetable();
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(parent1.getClasses().size());
        
        // Take first part from parent1
        for (int i = 0; i < crossoverPoint; i++) {
            child.addClass(parent1.getClasses().get(i));
        }
        
        // Take second part from parent2
        for (int i = crossoverPoint; i < parent2.getClasses().size(); i++) {
            child.addClass(parent2.getClasses().get(i));
        }
        
        return child;
    }

    private void mutate(Timetable timetable) {
        Random rand = new Random();
        List<TimeSlot> timeSlots = getAllTimeSlots();
        
        for (ScheduledClass sc : timetable.getClasses()) {
            if (rand.nextDouble() < MUTATION_RATE) {
                // Mutate time slot
                sc.setTimeSlot(timeSlots.get(rand.nextInt(timeSlots.size())));
            }
        }
    }
}