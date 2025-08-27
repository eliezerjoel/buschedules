package dev.eliezerjoelk.buschedules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Timetable;

@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {
}