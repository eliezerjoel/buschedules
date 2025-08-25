package dev.eliezerjoelk.buschedules.service.ga.operators;


import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.service.ga.TimetableChromosome;

// @Component
// public class MutationOperator {
    
//     public void mutate(TimetableChromosome chromosome, double mutationRate) {
//         Random random = new Random();
        
//         for (ScheduledClass scheduledClass : chromosome.getScheduledClasses()) {
//             if (random.nextDouble() < mutationRate) {
//                 // Randomly change instructor or time slot
//                 mutateScheduledClass(scheduledClass);
//             }
//         }
//     }
// }




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
            // Mutate instructor
            Instructor newInstructor = instructors.get(random.nextInt(instructors.size()));
            scheduledClass.setInstructor(newInstructor);
        } else {
            // Mutate time slot
            TimeSlot newTimeSlot = timeSlots.get(random.nextInt(timeSlots.size()));
            scheduledClass.setTimeSlot(newTimeSlot);
        }
    }
}