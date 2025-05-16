package dev.eliezerjoelk.buschedules.service;

public class SchedulingConflictException extends Exception {
    public SchedulingConflictException(String message) {
        super(message);
    }
}