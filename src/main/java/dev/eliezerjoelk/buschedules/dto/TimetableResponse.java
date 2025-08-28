package dev.eliezerjoelk.buschedules.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TimetableResponse {

    private List<DepartmentTimetable> departments; // Grouped by department
    private String message;
    private boolean success;
    private LocalDateTime generatedAt;
    private int totalClasses;
    private double fitnessScore;

    // Constructors
    public TimetableResponse() {}

    public TimetableResponse(List<DepartmentTimetable> departments, String message, boolean success) {
        this.departments = departments;
        this.message = message;
        this.success = success;
        this.generatedAt = LocalDateTime.now();
        this.totalClasses = departments != null
            ? departments.stream().mapToInt(d -> d.getClasses().size()).sum()
            : 0;
    }

    // Getters and Setters
    public List<DepartmentTimetable> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentTimetable> departments) {
        this.departments = departments;
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