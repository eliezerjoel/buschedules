package dev.eliezerjoelk.buschedules.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ScheduleAssignmentRequest {
    private String courseId;
    private String instructorId;
    private String studentGroupId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String semester;
    private String academicYear;
    
    // Constructors
    public ScheduleAssignmentRequest() {
    }
    
    public ScheduleAssignmentRequest(String courseId, String instructorId, String studentGroupId, DayOfWeek dayOfWeek, 
                                    LocalTime startTime, LocalTime endTime) {
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.studentGroupId = studentGroupId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and setters
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }
    public String getStudentGroupId() {
        return studentGroupId;
    }
    
    public void setStudentGroupId(String studentGroupId) {
        this.studentGroupId = studentGroupId;
    }
    
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semeter) {
        this.semester = semeter;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

}