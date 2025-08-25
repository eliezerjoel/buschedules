package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;

@Service
public class ScheduledClassService {
    private static final Logger logger = Logger.getLogger(ScheduledClassService.class.getName());

    @Autowired
    private ScheduledClassRepository scheduledClassRepository;

    public List<ScheduledClass> getAllScheduledClasses() {
        List<ScheduledClass> classes = scheduledClassRepository.findAll();
        logger.info("Retrieved " + classes.size() + " scheduled classes");
        return classes;
    }

    public Optional<ScheduledClass> getScheduledClassById(String id) {
        logger.info("Fetching scheduled class with ID: " + id);
        return scheduledClassRepository.findById(id);
    }

    public void deleteScheduledClass(String id) {
        logger.info("Deleting scheduled class with ID: " + id);
        scheduledClassRepository.deleteById(id);
    }

    public List<ScheduledClass> findScheduledClassByCourse(String courseId) {
        return scheduledClassRepository.findByCourseId(courseId);
    }

    public List<ScheduledClass> findScheduledClassByInstructor(String instructorId) {
        return scheduledClassRepository.findByInstructorId(instructorId);
    }

    public List<ScheduledClass> findScheduledClassByStudentGroup(String studentGroupId) { return scheduledClassRepository.findByStudentGroupId(studentGroupId);
    }
}