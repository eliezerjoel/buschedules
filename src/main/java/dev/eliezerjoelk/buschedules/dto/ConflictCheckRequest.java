package dev.eliezerjoelk.buschedules.dto;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ConflictCheckRequest {
    
    // @NotBlank(message = "Lecturer ID is required")
    // private String lecturerId;
    
    // @NotBlank(message = "Day of week is required")
    // @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$", 
    //          message = "Day must be a valid day of week (uppercase)")
    // private String dayOfWeek;
    
    // @NotBlank(message = "Start time is required")
    // @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$", 
    //          message = "Start time must be in format HH:MM or HH:MM:SS")
    // private String startTime;
    
    // @NotBlank(message = "End time is required")
    // @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$", 
    //          message = "End time must be in format HH:MM or HH:MM:SS")
    // private String endTime;
    
    private String studentGroupId;
    private String lecturerId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    
    // Default constructor
    public ConflictCheckRequest() {
    }
    
    // Getters and setters
    public String getLecturerId() {
        return lecturerId;
    }
    
    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getStudentGroupId() {
        return studentGroupId;
    }
    
    public void setStudentGroupId(String studentGroupId) {
        this.studentGroupId = studentGroupId;
    }
    
    // Helper methods
    public DayOfWeek parseDayOfWeek() {
        return DayOfWeek.valueOf(dayOfWeek);
    }
    
    public LocalTime parseStartTime() {
        return LocalTime.parse(startTime);
    }
    
    public LocalTime parseEndTime() {
        return LocalTime.parse(endTime);
    }
    
    // Validation method
    public boolean isValidTimeRange() {
        try {
            LocalTime start = parseStartTime();
            LocalTime end = parseEndTime();
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}