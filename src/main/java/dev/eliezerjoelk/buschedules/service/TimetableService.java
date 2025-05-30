package dev.eliezerjoelk.buschedules.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.exception.ResourceNotFoundException;
import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

@Service
public class TimetableService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    
    @Autowired
    private GeneticAlgorithmService geneticSchedulingService;
    
    @Autowired
    private TimeSlotService timeSlotService;

    /**
     * Generate optimal timetable using genetic algorithm
     */
    public List<ScheduledClass> generateOptimalTimetable() {
        return geneticSchedulingService.generateSchedule();
    }

    /**
     * Save generated timetable to database
     */
    public List<ScheduledClass> saveGeneratedTimetable() {
        List<ScheduledClass> generatedSchedule = generateOptimalTimetable();
        return scheduledClassRepository.saveAll(generatedSchedule);
    }

    /**
     * Get current timetable (all scheduled classes)
     */
    public List<ScheduledClass> getCurrentTimetable() {
        return scheduledClassRepository.findAll();
    }

    /**
     * Clear all scheduled classes
     */
    public void clearTimetable() {
        scheduledClassRepository.deleteAll();
    }

    /**
     * Get course by ID (helper method)
     */
    public Course getCourseById(String courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    /**
     * Get instructor by ID (helper method)
     */
    public Instructor getInstructorById(String instructorId) {
        return instructorRepository.findById(instructorId)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }
}