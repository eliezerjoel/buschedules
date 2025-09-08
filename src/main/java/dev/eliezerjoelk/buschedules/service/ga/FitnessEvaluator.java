package dev.eliezerjoelk.buschedules.service.ga;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;

@Service
public class FitnessEvaluator {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    public double evaluateFitness(TimetableChromosome chromosome) {
        double penalty = 0.0;
        
        // Hard constraints (high penalty)
        penalty += evaluateQualificationConflicts(chromosome) * 100;
        penalty += evaluateInstructorConflicts(chromosome) * 100;
        penalty += evaluateStudentGroupConflicts(chromosome) * 100;
        
        // Soft constraints (lower penalty)
        penalty += evaluateInstructorWorkloadDistribution(chromosome) * 10;
        penalty += evaluateTimePreferences(chromosome) * 5;
        
        return Math.max(0, 1000.0 - penalty); // Higher fitness = better solution
    }

    private double evaluateQualificationConflicts(TimetableChromosome chromosome) {
        int conflicts = 0;
        for (ScheduledClass scheduledClass : chromosome.getScheduledClasses()) {
            String courseId = scheduledClass.getCourse().getId();
            String instructorId = scheduledClass.getInstructor().getId();

            if (scheduledClass.getCourse().getInstructorIds() == null || 
                !scheduledClass.getCourse().getInstructorIds().contains(instructorId)) {
                conflicts++;
            }
        }
        return conflicts;
    }

    private double evaluateInstructorConflicts(TimetableChromosome chromosome) {
        // Check if instructor is assigned to multiple classes at same time
        int conflicts = 0;
        Map<String, List<ScheduledClass>> classesByInstructor = chromosome.getScheduledClasses().stream()
                .collect(Collectors.groupingBy(sc -> sc.getInstructor().getId()));

        for (List<ScheduledClass> classes : classesByInstructor.values()) {
            conflicts += countTimeConflicts(classes);
        }
        return conflicts;
    }

    private double evaluateStudentGroupConflicts(TimetableChromosome chromosome) {
        // Check if student group has overlapping classes
        int conflicts = 0;
        Map<String, List<ScheduledClass>> classesByStudentGroup = chromosome.getScheduledClasses().stream()
                .collect(Collectors.groupingBy(sc -> sc.getStudentGroup().getId()));

        for (List<ScheduledClass> classes : classesByStudentGroup.values()) {
            conflicts += countTimeConflicts(classes);
        }
        return conflicts;
    }
    
    private double evaluateInstructorWorkloadDistribution(TimetableChromosome chromosome) {
        Map<String, Long> instructorWorkload = chromosome.getScheduledClasses().stream()
            .collect(Collectors.groupingBy(sc -> sc.getInstructor().getId(), 
                                           Collectors.counting()));
                                           
        double totalWorkload = instructorWorkload.values().stream().mapToDouble(Long::doubleValue).sum();
        double avgWorkload = totalWorkload / instructorWorkload.size();
        
        return instructorWorkload.values().stream()
            .mapToDouble(workload -> Math.abs(workload - avgWorkload))
            .sum();
    }
    
    private double evaluateTimePreferences(TimetableChromosome chromosome) {
        // Penalize classes scheduled too early or too late
        return chromosome.getScheduledClasses().stream()
            .mapToDouble(sc -> {
                LocalTime startTime = sc.getTimeSlot().getStartTime();
                if (startTime.isBefore(LocalTime.of(8, 0)) || 
                    startTime.isAfter(LocalTime.of(18, 0))) {
                    return 1.0;
                }
                return 0.0;
            }).sum();
    }
    
    private int countTimeConflicts(List<ScheduledClass> classes) {
        int conflicts = 0;
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                if (timeSlotsOverlap(classes.get(i).getTimeSlot(), 
                                   classes.get(j).getTimeSlot())) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }
    
    private boolean timeSlotsOverlap(TimeSlot slot1, TimeSlot slot2) {
        if (!slot1.getDayOfWeek().equals(slot2.getDayOfWeek())) {
            return false;
        }
        
        return slot1.getStartTime().isBefore(slot2.getEndTime()) && 
               slot2.getStartTime().isBefore(slot1.getEndTime());
    }
}
