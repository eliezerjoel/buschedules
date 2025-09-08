package dev.eliezerjoelk.buschedules.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "instructors")
public class Instructor {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentId;
    private String qualifications; 
    private String office;
    private String employmentStatus;
    private List<String> courseIds;

    public Instructor() {
    }

    public Instructor(String firstName, String lastName, String email, String departmentId, String qualifications, String office, String employmentStatus, List<String> courseIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departmentId = departmentId;
        this.qualifications = qualifications;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    public String getEmploymentStatus() {
        return employmentStatus;
    }
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    public List<String> getCourseIds() {
        return courseIds;
    }
    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

}