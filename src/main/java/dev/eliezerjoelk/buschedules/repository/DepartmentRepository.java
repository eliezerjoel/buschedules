package dev.eliezerjoelk.buschedules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Department; // Make sure to import your Department model

@Repository // Mark this interface as a Spring Repository component
public interface DepartmentRepository extends MongoRepository<Department, String> {
    // MongoRepository provides implementations for:
    // - findAll()
    // - findById(String id)
    // - existsById(String id)
    // - deleteById(String id)
    // and many other CRUD operations automatically.
    // You do not need to write any code within this interface for those methods.

    // You can add custom query methods here if you need them,
    // for example:
    // Optional<Department> findByName(String name);
    // List<Department> findByCode(String code);
}