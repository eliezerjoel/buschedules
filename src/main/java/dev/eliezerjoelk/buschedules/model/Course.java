
package dev.eliezerjoelk.buschedules.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courses") 
public class Course {

    @Id
    private String id;
    private String courseCode;
    private String courseName;
    private int credits;
    @DBRef
    private List<Instructor> qualifiedInstructors;
    @DBRef
    private String departmentId; 

    // Constructors (default, all-args)
    public Course() {
    }
    public Course(String id, String courseCode, String courseName, int credits, List<Instructor> qualifiedInstructors, String departmentId) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.qualifiedInstructors = qualifiedInstructors;
        this.departmentId = departmentId;
    }

    // Getters and setters
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<Instructor> getQualifiedInstructors() {
        return qualifiedInstructors;
    }

    public void setQualifiedInstructors(List<Instructor> qualifiedInstructors) {
        this.qualifiedInstructors = qualifiedInstructors;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}