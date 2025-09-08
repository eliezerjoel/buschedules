
package dev.eliezerjoelk.buschedules.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courses") // Specifies the MongoDB collection name
public class Course {

    @Id
    private String id;
    private String courseCode;
    private String courseName;
    private int credits;
    private String departmentId;
    private List<String> instructorIds; 

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    // Constructors (default, all-args)
    public Course() {
    }

    public Course(String courseCode, String courseName, int credits, String departmentId, List<String> instructorIds) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.departmentId = departmentId;
        this.instructorIds = instructorIds;
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
    public List<String> getInstructorIds() {
        return instructorIds;
    }
    public void setInstructorIds(List<String> instructorIds) {
        this.instructorIds = instructorIds;
    }
    public String getDepartment() {
        return departmentId;
    }
    public void setDepartment(String departmentId) {
        this.departmentId = departmentId;
    }
}