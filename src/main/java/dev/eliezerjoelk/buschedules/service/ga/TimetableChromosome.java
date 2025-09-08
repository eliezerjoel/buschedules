package dev.eliezerjoelk.buschedules.service.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot;

@Component
public class TimetableChromosome {
    private List<ScheduledClass> scheduledClasses;
    private double fitness;
    
    public TimetableChromosome() {
        this.scheduledClasses = new ArrayList<>();
        this.fitness = 0.0;
    }
    
    public TimetableChromosome(List<Course> courses, List<Instructor> instructors, 
                              List<StudentGroup> studentGroups, List<TimeSlot> timeSlots) {
        this.scheduledClasses = generateRandomSchedule(courses, instructors, studentGroups, timeSlots);
        this.fitness = 0.0;
    }
    
    private List<ScheduledClass> generateRandomSchedule(List<Course> courses, List<Instructor> instructors, 
                                                       List<StudentGroup> studentGroups, List<TimeSlot> timeSlots) {
        List<ScheduledClass> schedule = new ArrayList<>();
        Random random = new Random();
        
        for (Course course : courses) {
            for (StudentGroup group : studentGroups) {
                ScheduledClass scheduledClass = new ScheduledClass();
                scheduledClass.setId(UUID.randomUUID().toString());
                scheduledClass.setCourse(course);
                scheduledClass.setStudentGroup(group);
                
                List<Instructor> qualifiedInstructors = getQualifiedInstructors(course, instructors);
                if (!qualifiedInstructors.isEmpty()) {
                    Instructor randomInstructor = qualifiedInstructors.get(random.nextInt(qualifiedInstructors.size()));
                    scheduledClass.setInstructor(randomInstructor);
                } else {
                    // Fallback: assign any instructor if no qualified ones found
                    Instructor randomInstructor = instructors.get(random.nextInt(instructors.size()));
                    scheduledClass.setInstructor(randomInstructor);
                }
                
                // Randomly assign time slot
                TimeSlot randomTimeSlot = timeSlots.get(random.nextInt(timeSlots.size()));
                scheduledClass.setTimeSlot(randomTimeSlot);
                
                schedule.add(scheduledClass);
            }
        }
        
        return schedule;
    }
    
    private List<Instructor> getQualifiedInstructors(Course course, List<Instructor> allInstructors) {
        if (course.getInstructorIds() == null || course.getInstructorIds().isEmpty()) {
            return allInstructors; // If no qualifications specified, all instructors are eligible
        }
        
        return allInstructors.stream()
            .filter(instructor -> course.getInstructorIds().contains(instructor.getId()))
            .collect(Collectors.toList());
    }
    
    // Getters and setters
    public List<ScheduledClass> getScheduledClasses() {
        return scheduledClasses;
    }
    
    public void setScheduledClasses(List<ScheduledClass> scheduledClasses) {
        this.scheduledClasses = scheduledClasses;
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
