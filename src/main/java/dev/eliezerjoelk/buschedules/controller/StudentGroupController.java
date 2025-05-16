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
import org.springframework.web.bind.annotation.RestController;

import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.service.StudentGroupService;

@RestController
@RequestMapping("/api/student-groups")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentGroupController {

    @Autowired
    private StudentGroupService studentGroupService;

    @GetMapping
    
@CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<StudentGroup>> getAllStudentGroups() {
        List<StudentGroup> studentGroups = studentGroupService.getAllStudentGroups();
        return new ResponseEntity<>(studentGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentGroup> getStudentGroupById(@PathVariable String id) {
        Optional<StudentGroup> studentGroup = studentGroupService.getStudentGroupById(id);
        return studentGroup.map(sg -> new ResponseEntity<>(sg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StudentGroup> createStudentGroup(@RequestBody StudentGroup studentGroup) {
        StudentGroup createdGroup = studentGroupService.createStudentGroup(studentGroup);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentGroup> updateStudentGroup(@PathVariable String id, @RequestBody StudentGroup updatedGroup) {
        Optional<StudentGroup> updated = studentGroupService.updateStudentGroup(id, updatedGroup);
        return updated.map(sg -> new ResponseEntity<>(sg, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentGroup(@PathVariable String id) {
        studentGroupService.deleteStudentGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}