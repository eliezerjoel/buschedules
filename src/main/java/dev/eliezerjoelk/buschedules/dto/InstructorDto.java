package dev.eliezerjoelk.buschedules.dto;

import java.util.List;
import dev.eliezerjoelk.buschedules.model.Instructor;

public class InstructorDto {
    private Instructor instructor;
    private List<String> courseIds;

    // Getters and Setters
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }
}