package dev.eliezerjoelk.buschedules.model;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private String id;
    private List<ScheduledClass> classes;

    public Timetable() {
        this.classes = new ArrayList<>();
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void addClass(ScheduledClass scheduledClass) {
        classes.add(scheduledClass);
    }

    public List<ScheduledClass> getClasses() {
        return classes;
    }

    public void setClasses(List<ScheduledClass> classes) {
        this.classes = classes;
    }
}
