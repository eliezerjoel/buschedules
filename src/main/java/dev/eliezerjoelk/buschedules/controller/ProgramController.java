package dev.eliezerjoelk.buschedules.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.Program;
import dev.eliezerjoelk.buschedules.service.ProgramService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/programs")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms(@RequestParam(required = false) String departmentId) {
        if (departmentId != null && !departmentId.isEmpty()) {
            // If departmentId is provided, return programs for that department
            List<Program> programs = programService.findByDepartmentId(departmentId);
            return new ResponseEntity<>(programs, HttpStatus.OK);
        } else {
            // Otherwise, return all programs
            List<Program> programs = programService.getAllPrograms();
            return new ResponseEntity<>(programs, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable String id) {
        Optional<Program> program = programService.getProgramById(id);
        return program.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Program> createProgram(@RequestBody Program program) {
        Program createdProgram = programService.createProgram(program);
        return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(@PathVariable String id, @RequestBody Program updatedProgram) {
        Optional<Program> updated = programService.updateProgram(id, updatedProgram);
        return updated.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
        boolean deleted = programService.deleteProgram(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}