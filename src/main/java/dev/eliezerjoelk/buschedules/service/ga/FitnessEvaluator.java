// @Service
// public class FitnessEvaluator {
    
//     public double evaluateFitness(TimetableChromosome chromosome) {
//         double penalty = 0.0;
        
//         // Hard constraints (high penalty)
//         penalty += evaluateInstructorConflicts(chromosome) * 100;
//         penalty += evaluateStudentGroupConflicts(chromosome) * 100;
        
//         // Soft constraints (lower penalty)
//         penalty += evaluateInstructorWorkloadDistribution(chromosome) * 10;
//         penalty += evaluateTimePreferences(chromosome) * 5;
        
//         return 1000.0 - penalty; // Higher fitness = better solution
//     }
    
//     private double evaluateInstructorConflicts(TimetableChromosome chromosome) {
//         // Check if instructor is assigned to multiple classes at same time
//     }
    
//     private double evaluateStudentGroupConflicts(TimetableChromosome chromosome) {
//         // Check if student group has overlapping classes
//     }
// }

package dev.eliezerjoelk.buschedules.service.ga;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.ga.constraints.ConstraintValidator;

@Service
public class FitnessEvaluator {
    
    @Autowired
    private ConstraintValidator constraintValidator;
    
    public double evaluateFitness(TimetableChromosome chromosome) {
        double penalty = 0.0;
        
        // Hard constraints (high penalty)
        penalty += evaluateInstructorConflicts(chromosome) * 100;
        penalty += evaluateStudentGroupConflicts(chromosome) * 100;
        
        // Soft constraints (lower penalty)
        penalty += evaluateInstructorWorkloadDistribution(chromosome) * 10;
        penalty += evaluateTimePreferences(chromosome) * 5;
        
        return 1000.0 - penalty;
    }
    
    private double evaluateInstructorConflicts(TimetableChromosome chromosome) {
        Map<String, List<ScheduledClass>> instructorClasses = 
            chromosome.getScheduledClasses().stream()
                .collect(Collectors.groupingBy(sc -> sc.getInstructor().getId()));
        
        int conflicts = 0;
        for (List<ScheduledClass> classes : instructorClasses.values()) {
            conflicts += countTimeConflicts(classes);
        }
        
        return conflicts;
    }
    
    private double evaluateStudentGroupConflicts(TimetableChromosome chromosome) {
        Map<String, List<ScheduledClass>> groupClasses = 
            chromosome.getScheduledClasses().stream()
                .collect(Collectors.groupingBy(sc -> sc.getStudentGroup().getId()));
        
        int conflicts = 0;
        for (List<ScheduledClass> classes : groupClasses.values()) {
            conflicts += countTimeConflicts(classes);
        }
        
        return conflicts;
    }
    
    private double evaluateInstructorWorkloadDistribution(TimetableChromosome chromosome) {
        Map<String, Long> instructorWorkload = 
            chromosome.getScheduledClasses().stream()
                .collect(Collectors.groupingBy(
                    sc -> sc.getInstructor().getId(), 
                    Collectors.counting()));
        
        double avgWorkload = instructorWorkload.values().stream()
            .mapToLong(Long::longValue).average().orElse(0.0);
        
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