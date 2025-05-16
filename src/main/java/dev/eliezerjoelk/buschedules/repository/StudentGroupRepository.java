package dev.eliezerjoelk.buschedules.repository;

import dev.eliezerjoelk.buschedules.model.StudentGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends MongoRepository<StudentGroup, String> {
    // You can add custom query methods here if needed
}