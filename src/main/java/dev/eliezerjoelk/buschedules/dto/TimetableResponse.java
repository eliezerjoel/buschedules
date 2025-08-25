package dev.eliezerjoelk.buschedules.dto;

import dev.eliezerjoelk.buschedules.model.Timetable;
import java.time.LocalDateTime;

public class TimetableResponse {
    
    private Timetable timetable;
    private String message;
    private boolean success;
    private LocalDateTime generatedAt;
    private int totalClasses;
    private double fitnessScore;
    
    // Constructors
    public TimetableResponse() {}
    
    public TimetableResponse(Timetable timetable, String message, boolean success) {
        this.timetable = timetable;
        this.message = message;
        this.success = success;
        this.generatedAt = LocalDateTime.now();
        this.totalClasses = timetable != null ? timetable.getClasses().size() : 0;
    }
    
    // Getters and Setters
    public Timetable getTimetable() {
        return timetable;
    }
    
    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public int getTotalClasses() {
        return totalClasses;
    }
    
    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public double getFitnessScore() {
        return fitnessScore;
    }
    
    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }
}