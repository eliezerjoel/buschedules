// dev.eliezerjoelk.buschedules.dto.ScheduledClassDto.java

package dev.eliezerjoelk.buschedules.dto;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.model.Instructor;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class ScheduledClassDto {
    private String id;
    private String courseCode;
    private String courseName;
    private String instructor;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String departmentId;

    public ScheduledClassDto(String id, String courseCode, String courseName, String instructor, String departmentId, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.departmentId = departmentId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    // ...
}