package dev.eliezerjoelk.buschedules.model;

import java.time.DayOfWeek;
import java.util.Map;

public class TimetableStatistics {
    private int totalClasses;
    private Map<String, Long> instructorWorkload;
    private Map<DayOfWeek, Long> dailyClassCount;
    
    public TimetableStatistics(int totalClasses, Map<String, Long> instructorWorkload, 
                              Map<DayOfWeek, Long> dailyClassCount) {
        this.totalClasses = totalClasses;
        this.instructorWorkload = instructorWorkload;
        this.dailyClassCount = dailyClassCount;
    }
    
    // Getters and setters
    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }
    
    public Map<String, Long> getInstructorWorkload() { return instructorWorkload; }
    public void setInstructorWorkload(Map<String, Long> instructorWorkload) { this.instructorWorkload = instructorWorkload; }
    
    public Map<DayOfWeek, Long> getDailyClassCount() { return dailyClassCount; }
    public void setDailyClassCount(Map<DayOfWeek, Long> dailyClassCount) { this.dailyClassCount = dailyClassCount; }
}