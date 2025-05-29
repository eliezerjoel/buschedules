package dev.eliezerjoelk.buschedules.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "departments") // Maps this class to a MongoDB collection named "departments"
public class Department {

    @Id // Marks this field as the primary key for MongoDB
    private String id;
    private String name;
    private String code;

    // Default constructor (required by Spring Data MongoDB)
    public Department() {
    }

    // Constructor with all fields (optional, but often useful)
    public Department(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
}