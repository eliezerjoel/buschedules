package dev.eliezerjoelk.buschedules.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scheduled_classes")
public class ScheduledClass {

    @Id
    private String id;
    @DBRef
    private Course course;
    @DBRef
    private Instructor instructor;
    @DBRef
    private StudentGroup studentGroup;
    private TimeSlot timeSlot;
    // private DayOfWeek dayOfWeek; 
    // private LocalTime startTime; 
    // private LocalTime endTime;   
    private String semester; 
    private String academicYear; 

    public ScheduledClass() {
    }

    public ScheduledClass(Course course, Instructor instructor, StudentGroup studentGroup,TimeSlot timeSlot, String semester, String academicYear) {


    
        // public ScheduledClass(Course course, Instructor instructor, StudentGroup studentGroup, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String semester, String academicYear) {
        this.course = course;
        this.instructor = instructor;
        this.studentGroup = studentGroup;
        // this.dayOfWeek = dayOfWeek;
        // this.startTime = startTime;
        // this.endTime = endTime;
        this.timeSlot = timeSlot;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    // Getters and setters
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

    // public DayOfWeek getDayOfWeek() {
    //     return dayOfWeek;
    // }

    // public void setDayOfWeek(DayOfWeek dayOfWeek2) {
    //     this.dayOfWeek = dayOfWeek2;
    // }

    // public LocalTime getStartTime() {
    //     return startTime;
    // }

    // public void setStartTime(LocalTime startTime) {
    //     this.startTime = startTime;
    // }

    // public LocalTime getEndTime() {
    //     return endTime;
    // }

    // public void setEndTime(LocalTime endTime) {
    //     this.endTime = endTime;
    // }


    
    public TimeSlot gettimeSlot() {
        return timeSlot;
    }

    public void settimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}