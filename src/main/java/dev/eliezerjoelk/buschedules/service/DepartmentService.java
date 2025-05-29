package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Department;
import dev.eliezerjoelk.buschedules.repository.DepartmentRepository; 

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(String id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Optional<Department> updateDepartment(String id, Department updatedDepartment) {
        return departmentRepository.findById(id)
                .map(department -> {
                    // Update the fields of the existing department
                    // Assuming Department model has setName and setCode methods
                    department.setName(updatedDepartment.getName());
                    department.setCode(updatedDepartment.getCode());
                    // You might want to update other fields if they exist in Department model
                    return departmentRepository.save(department); // Save the updated department
                });
    }

    public boolean deleteDepartment(String id) {
        if (departmentRepository.existsById(id)) { // Check if the department exists
            departmentRepository.deleteById(id); // Delete by ID
            return true; // Indicate successful deletion
        }
        return false; // Indicate department not found
    }
}