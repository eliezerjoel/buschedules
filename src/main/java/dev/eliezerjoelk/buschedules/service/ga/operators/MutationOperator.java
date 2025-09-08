package dev.eliezerjoelk.buschedules.service.ga.operators;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.ga.TimetableChromosome;

@Component
public class MutationOperator {
    
    public void mutate(TimetableChromosome chromosome, double mutationRate, 
                      List<Instructor> instructors, List<TimeSlot> timeSlots) {
        Random random = new Random();
        
        for (ScheduledClass scheduledClass : chromosome.getScheduledClasses()) {
            if (random.nextDouble() < mutationRate) {
                mutateScheduledClass(scheduledClass, instructors, timeSlots);
            }
        }
    }
    
    private void mutateScheduledClass(ScheduledClass scheduledClass, 
                                    List<Instructor> instructors, List<TimeSlot> timeSlots) {
        Random random = new Random();
        
        // 50% chance to mutate instructor, 50% chance to mutate time slot
        if (random.nextBoolean()) {
            List<Instructor> qualifiedInstructors = getQualifiedInstructors(scheduledClass, instructors);
            if (!qualifiedInstructors.isEmpty()) {
                Instructor newInstructor = qualifiedInstructors.get(random.nextInt(qualifiedInstructors.size()));
                scheduledClass.setInstructor(newInstructor);
            }
            // If no qualified instructors, keep current assignment
        } else {
            // Mutate time slot
            TimeSlot newTimeSlot = timeSlots.get(random.nextInt(timeSlots.size()));
            scheduledClass.setTimeSlot(newTimeSlot);
        }
    }
    
    private List<Instructor> getQualifiedInstructors(ScheduledClass scheduledClass, List<Instructor> allInstructors) {
        if (scheduledClass.getCourse().getInstructorIds() == null || 
            scheduledClass.getCourse().getInstructorIds().isEmpty()) {
            return allInstructors; // If no qualifications specified, all instructors are eligible
        }
        
        return allInstructors.stream()
            .filter(instructor -> scheduledClass.getCourse().getInstructorIds().contains(instructor.getId()))
            .collect(Collectors.toList());
    }
}
