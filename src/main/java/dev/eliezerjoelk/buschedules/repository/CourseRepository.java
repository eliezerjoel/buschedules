package dev.eliezerjoelk.buschedules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    // Custom query methods can be defined here if needed
}
