package dev.eliezerjoelk.buschedules.dto;

import dev.eliezerjoelk.buschedules.model.Course;
import java.util.List;

public class DepartmentTimetable {
    private String departmentId;
    private List<Course> courses;

    public DepartmentTimetable(String departmentId, List<Course> courses) {
        this.departmentId = departmentId;
        this.courses = courses;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}