// dev.eliezerjoelk.buschedules.dto.DepartmentTimetable.java

package dev.eliezerjoelk.buschedules.dto;

import java.util.List;

public class DepartmentTimetable {
    private String departmentId;
    private String departmentName;
    private List<ScheduledClassDto> classes; // <-- Changed to ScheduledClassDto

    public DepartmentTimetable(String departmentId, String departmentName, List<ScheduledClassDto> classes) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.classes = classes;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public List<ScheduledClassDto> getClasses() {
        return classes;
    }
}