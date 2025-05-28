package dev.eliezerjoelk.buschedules.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "programs") // Maps this class to a MongoDB collection named "programs"
public class Program {

    @Id // Marks this field as the primary key for MongoDB
    private String id;
    private String name; // e.g., "Bachelor of Business Computing"
    private String shortCode; // e.g., "BCC", "BSE"
    private Integer creditsRequired; // Total credits needed for the program

    @DBRef // Creates a database reference to the Department document
    private Department department; // The department offering this program

    // Default constructor (required by Spring Data MongoDB)
    public Program() {
    }

    // Constructor with all fields (optional, but often useful)
    public Program(String id, String name, String shortCode, Integer creditsRequired, Department department) {
        this.id = id;
        this.name = name;
        this.shortCode = shortCode;
        this.creditsRequired = creditsRequired;
        this.department = department;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getCreditsRequired() {
        return creditsRequired;
    }

    public void setCreditsRequired(Integer creditsRequired) {
        this.creditsRequired = creditsRequired;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Program{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", shortCode='" + shortCode + '\'' +
               ", creditsRequired=" + creditsRequired +
               ", department=" + (department != null ? department.getName() : "null") +
               '}';
    }
}