package dev.eliezerjoelk.buschedules.model;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private List<ScheduledClass> classes;

    public Timetable() {
        this.classes = new ArrayList<>();
    }

    public void addClass(ScheduledClass scheduledClass) {
        classes.add(scheduledClass);
    }

    public List<ScheduledClass> getClasses() {
        return classes;
    }
}