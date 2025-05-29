package dev.eliezerjoelk.buschedules.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.service.GeneticAlgorithmService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    @Autowired
    private GeneticAlgorithmService gaService;

    @PostMapping("/generate")
    public ResponseEntity<List<ScheduledClass>> generateAutomatedSchedule() {
        List<ScheduledClass> schedule = gaService.generateSchedule();
        return ResponseEntity.ok(schedule);
    }
}