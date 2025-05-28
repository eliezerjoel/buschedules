package dev.eliezerjoelk.buschedules.controller;

import dev.eliezerjoelk.buschedules.model.Department;
import dev.eliezerjoelk.buschedules.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable String id, @RequestBody Department updatedDepartment) {
        Optional<Department> updated = departmentService.updateDepartment(id, updatedDepartment);
        return updated.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Completing the update method
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        boolean deleted = departmentService.deleteDepartment(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if department doesn't exist
        }
    }
}








































//package dev.eliezerjoelk.buschedules.controller;

// import dev.eliezerjoelk.buschedules.model.Department;
// import dev.eliezerjoelk.buschedules.service.DepartmentService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @RequestMapping("/api/departments")
// public class DepartmentController {

//     @Autowired
//     private DepartmentService departmentService;

//     @GetMapping
//     public ResponseEntity<List<Department>> getAllDepartments() {
//         List<Department> departments = departmentService.getAllDepartments();
//         return new ResponseEntity<>(departments, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Department> getDepartmentById(@PathVariable String id) {
//         Optional<Department> department = departmentService.getDepartmentById(id);
//         return department.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
//                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//     }

//     @PostMapping
//     public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
//         Department createdDepartment = departmentService.createDepartment(department);
//         return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Department> updateDepartment(@PathVariable String id, @RequestBody Department updatedDepartment) {
//         Optional<Department> updated = departmentService.updateDepartment(id, updatedDepartment);
//         return updated.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
//                 .or