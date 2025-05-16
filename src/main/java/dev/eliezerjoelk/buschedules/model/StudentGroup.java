package dev.eliezerjoelk.buschedules.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "student_groups")
public class StudentGroup {

    @Id
    private String id;
    private String groupName;
    private String program;
    private int numberOfStudents;

    // Constructors (default, all-args)
    public StudentGroup() {
    }

    public StudentGroup(String groupName, String program, int numberOfStudents) {
        this.groupName = groupName;
        this.program = program;
        this.numberOfStudents = numberOfStudents;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}