package dev.eliezerjoelk.buschedules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Instructor;

@Repository
public interface InstructorRepository extends MongoRepository<Instructor, String> {
    // You can add custom query methods here if needed
}