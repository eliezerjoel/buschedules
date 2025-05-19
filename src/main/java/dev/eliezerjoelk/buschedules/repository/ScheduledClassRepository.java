
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
}


// package dev.eliezerjoelk.buschedules.repository;

// import java.util.List;

// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.stereotype.Repository;

// import dev.eliezerjoelk.buschedules.model.ScheduledClass;

// @Repository
// public interface ScheduledClassRepository extends MongoRepository<ScheduledClass, String> {
//     // You can add custom query methods here if needed, e.g.,
//     // List<ScheduledClass> findByCourse(Course course);
//     // List<ScheduledClass> findByInstructor(Instructor instructor);
//     // List<ScheduledClass> findByStudentGroup(StudentGroup studentGroup);
//     // List<ScheduledClass> findByDayOfWeekAndStartTime(String dayOfWeek, String startTime);

    
//     List<ScheduledClass> findByInstructorId(String instructorId);
// }

// In your src/main/java/dev/eliezerjoelk/buschedules/repository/ScheduledClassRepository.java
