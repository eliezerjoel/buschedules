package dev.eliezerjoelk.buschedules.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "departments") // Maps this class to a MongoDB collection named "departments"
public class Department {

    @Id 
    private String id;
    private String name;
    private String code;
    private String headOfDepartment;

    // Default constructor 
    public Department() {
    }

    // Constructor with all fields 
    public Department(String id, String name, String code, String headOfDepartment) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.headOfDepartment = headOfDepartment;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Department{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", code='" + code + '\'' +
               '}';
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }
}