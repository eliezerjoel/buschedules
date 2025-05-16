package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(String id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCourseCode(updatedCourse.getCourseCode());
                    course.setCourseName(updatedCourse.getCourseName());
                    course.setCredits(updatedCourse.getCredits());
                    return courseRepository.save(course);
                });
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }
}