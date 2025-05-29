package dev.eliezerjoelk.buschedules.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Program; // Import the Program model

@Repository // Mark this interface as a Spring Repository component
public interface ProgramRepository extends MongoRepository<Program, String> {

    List<Program> findByDepartmentId(String departmentId);


















    
    // MongoRepository provides CRUD operations (findAll, findById, save, deleteById, existsById)
    // You can add custom query methods here if needed, for example:
    // List<Program> findByDepartmentId(String departmentId); // To support fetching programs by department
    // Optional<Program> findByShortCode(String shortCode);
}