package dev.eliezerjoelk.buschedules.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;

@Repository
public interface ScheduledClassRepository extends MongoRepository<ScheduledClass, String> {
    // You can add custom query methods here if needed, e.g.,
    // List<ScheduledClass> findByCourse(Course course);
    // List<ScheduledClass> findByInstructor(Instructor instructor);
    // List<ScheduledClass> findByStudentGroup(StudentGroup studentGroup);
    // List<ScheduledClass> findByDayOfWeekAndStartTime(String dayOfWeek, String startTime);

    
    List<ScheduledClass> findByInstructorId(String instructorId);
}