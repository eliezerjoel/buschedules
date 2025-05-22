
package dev.eliezerjoelk.buschedules.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository; // Make sure to import StudentGroup

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.StudentGroup;

@Repository
public interface ScheduledClassRepository extends MongoRepository<ScheduledClass, String> {

    // Method to find scheduled classes by Instructor and Day of Week
    List<ScheduledClass> findByInstructorAndDayOfWeek(Instructor instructor, DayOfWeek dayOfWeek);

    // Method to find scheduled classes by StudentGroup and Day of Week
    List<ScheduledClass> findByStudentGroupAndDayOfWeek(StudentGroup studentGroup, DayOfWeek dayOfWeek);

    // Method to find all scheduled classes for a given instructor (regardless of day)
    List<ScheduledClass> findByInstructor(Instructor instructor);

    // If you need to find by StudentGroup only, add this:
    List<ScheduledClass> findByStudentGroup(StudentGroup studentGroup);

    // Method to find all scheduled classes
    // List<ScheduledClass> findAll();
}

