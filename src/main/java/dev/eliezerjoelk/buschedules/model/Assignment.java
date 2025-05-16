package dev.eliezerjoelk.buschedules.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assignments")
public class Assignment {
    
    @Id
    private String id;
    
    @DBRef
    private Course course;
    
    @DBRef
    private Instructor instructor;
    
    @DBRef
    private StudentGroup studentGroup;
    
    private String room;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    
    // Constructors
    public Assignment() {
    }
    
    public Assignment(Course course, Instructor instructor, StudentGroup studentGroup, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.course = course;
        this.instructor = instructor;
        this.studentGroup = studentGroup;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Instructor getInstructor() {
        return instructor;
    }
    
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    
    public StudentGroup getStudentGroup() {
        return studentGroup;
    }
    
    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }
    
    public String getRoom() {
        return room;
    }
    
    public void setRoom(String room) {
        this.room = room;
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
    
    // Helper methods
    public String getCourseId() {
        return course != null ? course.getId() : null;
    }
    
    public String getInstructorId() {
        return instructor != null ? instructor.getId() : null;
    }
    
    public String getStudentGroupId() {
        return studentGroup != null ? studentGroup.getId() : null;
    }
    
    @Override
    public String toString() {
        return "Assignment [id=" + id + ", course=" + course + ", instructor=" + instructor + 
               ", studentGroup=" + studentGroup + ", room=" + room + ", dayOfWeek=" + dayOfWeek +
               ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }
}