package dev.eliezerjoelk.buschedules.service.ga.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.service.ga.TimetableChromosome;

@Component
public class CrossoverOperator {
    
    public TimetableChromosome crossover(TimetableChromosome parent1, TimetableChromosome parent2) {
        TimetableChromosome offspring = new TimetableChromosome();
        List<ScheduledClass> offspringClasses = new ArrayList<>();
        
        List<ScheduledClass> parent1Classes = parent1.getScheduledClasses();
        List<ScheduledClass> parent2Classes = parent2.getScheduledClasses();
        
        int crossoverPoint = parent1Classes.size() / 2;
        
        // Take first half from parent1
        for (int i = 0; i < crossoverPoint; i++) {
            offspringClasses.add(copyScheduledClass(parent1Classes.get(i)));
        }
        
        // Take second half from parent2
        for (int i = crossoverPoint; i < parent2Classes.size(); i++) {
            offspringClasses.add(copyScheduledClass(parent2Classes.get(i)));
        }
        
        offspring.setScheduledClasses(offspringClasses);
        
        repairQualificationViolations(offspring);
        
        return offspring;
    }
    
    private ScheduledClass copyScheduledClass(ScheduledClass original) {
        ScheduledClass copy = new ScheduledClass();
        copy.setId(UUID.randomUUID().toString());
        copy.setCourse(original.getCourse());
        copy.setInstructor(original.getInstructor());
        copy.setStudentGroup(original.getStudentGroup());
        copy.setTimeSlot(original.getTimeSlot());
        copy.setSemester(original.getSemester());
        copy.setAcademicYear(original.getAcademicYear());
        return copy;
    }
    
    private void repairQualificationViolations(TimetableChromosome chromosome) {
        Random random = new Random();
        
        for (ScheduledClass scheduledClass : chromosome.getScheduledClasses()) {
            // Check if current instructor is qualified
            if (scheduledClass.getCourse().getInstructorIds() != null && 
                !scheduledClass.getCourse().getInstructorIds().isEmpty() &&
                !scheduledClass.getCourse().getInstructorIds().contains(scheduledClass.getInstructor().getId())) {
                
                // Find a qualified instructor from the course's instructor list
                // This requires access to all instructors, which we don't have here
                // So we'll leave this for the mutation operator to fix
                // The fitness evaluator will penalize this violation
            }
        }
    }
}
