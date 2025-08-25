package dev.eliezerjoelk.buschedules.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Timetable;

@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {
    // Custom query methods can be defined here if needed
    // Optional<Timetable> findBySemesterAndAcademicYear(String semester, String academicYear);
    // List<Timetable> findByAcademicYear(String academicYear);
    // List<Timetable> findBySemester(String semester);
}