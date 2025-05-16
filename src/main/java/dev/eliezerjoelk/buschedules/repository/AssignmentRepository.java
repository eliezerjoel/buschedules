package dev.eliezerjoelk.buschedules.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.eliezerjoelk.buschedules.model.Assignment;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    
    // Find assignments by course ID
    @Query("{'course.$id': ObjectId(?0)}")
    List<Assignment> findByCourseId(String courseId);
    
    // Find assignments by instructor ID
    @Query("{'instructor.$id': ObjectId(?0)}")
    List<Assignment> findByInstructorId(String instructorId);
    
    // Find assignments by student group ID
    @Query("{'studentGroup.$id': ObjectId(?0)}")
    List<Assignment> findByStudentGroupId(String studentGroupId);
    
    // Find assignments by room
    List<Assignment> findByRoom(String room);
    
    // Find assignments by day of week
    List<Assignment> findByDayOfWeek(DayOfWeek dayOfWeek);
    
    // Find assignments by course ID and instructor ID
    @Query("{'course.$id': ObjectId(?0), 'instructor.$id': ObjectId(?1)}")
    List<Assignment> findByCourseIdAndInstructorId(String courseId, String instructorId);
    
    // Find assignments by day and instructor ID (for checking instructor availability on a specific day)
    @Query("{'dayOfWeek': ?0, 'instructor.$id': ObjectId(?1)}")
    List<Assignment> findByDayOfWeekAndInstructorId(DayOfWeek dayOfWeek, String instructorId);
    
    // Find assignments by day and course ID (for checking if a course is already scheduled on a specific day)
    @Query("{'dayOfWeek': ?0, 'course.$id': ObjectId(?1)}")
    List<Assignment> findByDayOfWeekAndCourseId(DayOfWeek dayOfWeek, String courseId);
}