package dev.eliezerjoelk.buschedules.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(String id) {
        return instructorRepository.findById(id);
    }

    public Instructor createInstructor(Instructor instructor, String departmentId, List<String> courseIds) {
        instructor.setDepartmentId(departmentId);
        instructor.setCourseIds(courseIds);
        
        Instructor newInstructor = instructorRepository.save(instructor);

        if (courseIds != null && !courseIds.isEmpty()) {
            List<Course> coursesToUpdate = courseRepository.findAllById(courseIds);
            for (Course course : coursesToUpdate) {
                if (course.getInstructorIds() == null) {
                    course.setInstructorIds(new ArrayList<>());
                }
                if (!course.getInstructorIds().contains(newInstructor.getId())) {
                    course.getInstructorIds().add(newInstructor.getId());
                }
            }
            courseRepository.saveAll(coursesToUpdate);
        }
        return newInstructor;
    }

    public Optional<Instructor> updateInstructor(String id, Instructor updatedInstructor, String departmentId, List<String> courseIds) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    instructor.setFirstName(updatedInstructor.getFirstName());
                    instructor.setLastName(updatedInstructor.getLastName());
                    instructor.setEmail(updatedInstructor.getEmail());
                    // instructor.setPhone(updatedInstructor.getPhone());
                    instructor.setQualifications(updatedInstructor.getQualifications());
                    instructor.setOffice(updatedInstructor.getOffice());
                    instructor.setEmploymentStatus(updatedInstructor.getEmploymentStatus());

                    instructor.setDepartmentId(departmentId);
                    instructor.setCourseIds(courseIds);

                    Instructor savedInstructor = instructorRepository.save(instructor);

                    if (courseIds != null && !courseIds.isEmpty()) {
                        List<Course> coursesToUpdate = courseRepository.findAllById(courseIds);
                        for (Course course : coursesToUpdate) {
                            if (course.getInstructorIds() == null) {
                                course.setInstructorIds(new ArrayList<>());
                            }
                            if (!course.getInstructorIds().contains(savedInstructor.getId())) {
                                course.getInstructorIds().add(savedInstructor.getId());
                            }
                        }
                        courseRepository.saveAll(coursesToUpdate);
                    }
                    return savedInstructor;
                });
    }

    public void deleteInstructor(String id) {
        List<Course> courses = courseRepository.findByInstructorIdsContaining(id);
        for (Course course : courses) {
            course.getInstructorIds().remove(id);
        }
        courseRepository.saveAll(courses);
        
        instructorRepository.deleteById(id);
    }

    public List<Instructor> getAvailableInstructorsForCourse(String courseId) {
        // Find instructors who are not assigned to the given course
        return instructorRepository.findAll().stream()
                .filter(instructor -> instructor.getCourseIds() == null || !instructor.getCourseIds().contains(courseId))
                .collect(Collectors.toList());
    }
}