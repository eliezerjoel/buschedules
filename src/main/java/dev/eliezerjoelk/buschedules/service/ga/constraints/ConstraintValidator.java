package dev.eliezerjoelk.buschedules.service.ga.constraints;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.TimeSlot;

// @Component
// public class ConstraintValidator {
    
//     public boolean isValidTimetable(List<ScheduledClass> classes) {
//         return !hasInstructorConflicts(classes) && 
//                !hasStudentGroupConflicts(classes) &&
//                hasValidInstructorAssignments(classes);
//     }
    
//     private boolean hasInstructorConflicts(List<ScheduledClass> classes) {
//         // Group by instructor and check for time overlaps
//         Map<String, List<ScheduledClass>> instructorClasses = 
//             classes.stream().collect(Collectors.groupingBy(
//                 sc -> sc.getInstructor().getId()));
                
//         return instructorClasses.values().stream()
//             .anyMatch(this::hasTimeConflicts);
//     }
// }


@Component
public class ConstraintValidator {
    
    public boolean isValidTimetable(List<ScheduledClass> classes) {
        return !hasInstructorConflicts(classes) && 
               !hasStudentGroupConflicts(classes) &&
               hasValidInstructorAssignments(classes);
    }
    
    private boolean hasInstructorConflicts(List<ScheduledClass> classes) {
        Map<String, List<ScheduledClass>> instructorClasses = 
            classes.stream().collect(Collectors.groupingBy(
                sc -> sc.getInstructor().getId()));
                
        return instructorClasses.values().stream()
            .anyMatch(this::hasTimeConflicts);
    }
    
    private boolean hasStudentGroupConflicts(List<ScheduledClass> classes) {
        Map<String, List<ScheduledClass>> groupClasses = 
            classes.stream().collect(Collectors.groupingBy(
                sc -> sc.getStudentGroup().getId()));
                
        return groupClasses.values().stream()
            .anyMatch(this::hasTimeConflicts);
    }
    
    private boolean hasValidInstructorAssignments(List<ScheduledClass> classes) {
        return classes.stream().allMatch(sc -> 
            sc.getInstructor() != null && sc.getTimeSlot() != null);
    }
    
    private boolean hasTimeConflicts(List<ScheduledClass> classList) {
        for (int i = 0; i < classList.size(); i++) {
            for (int j = i + 1; j < classList.size(); j++) {
                if (timeSlotsOverlap(classList.get(i).getTimeSlot(), 
                                   classList.get(j).getTimeSlot())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean timeSlotsOverlap(TimeSlot slot1, TimeSlot slot2) {
        if (!slot1.getDayOfWeek().equals(slot2.getDayOfWeek())) {
            return false;
        }
        
        return slot1.getStartTime().isBefore(slot2.getEndTime()) && 
               slot2.getStartTime().isBefore(slot1.getEndTime());
    }
}