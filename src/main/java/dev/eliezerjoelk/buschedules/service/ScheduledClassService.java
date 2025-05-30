package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;

@Service
public class ScheduledClassService {
    private static final Logger logger = Logger.getLogger(ScheduledClassService.class.getName());

    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private StudentGroupRepository studentGroupRepository;
    @Autowired
    private GeneticAlgorithmService geneticSchedulingService;

    // Basic CRUD operations
    public List<ScheduledClass> getAllScheduledClasses() {
        return scheduledClassRepository.findAll();
    }

    public Optional<ScheduledClass> getScheduledClassById(String id) {
        return scheduledClassRepository.findById(id);
    }

    public void deleteScheduledClass(String id) {
        scheduledClassRepository.deleteById(id);
    }

    // Genetic Algorithm integration
    public List<ScheduledClass> generateOptimalSchedule() {
        return geneticSchedulingService.generateSchedule();
    }

    public List<ScheduledClass> saveGeneratedSchedule() {
        List<ScheduledClass> generated = generateOptimalSchedule();
        return scheduledClassRepository.saveAll(generated);
    }

    public void clearAllSchedules() {
        scheduledClassRepository.deleteAll();
    }

    // Helper methods for data access
    public List<ScheduledClass> findByCourse(String courseId) {
        return scheduledClassRepository.findByCourseId(courseId);
    }

    public List<ScheduledClass> findByInstructor(String instructorId) {
        return scheduledClassRepository.findByInstructorId(instructorId);
    }

    public List<ScheduledClass> findByStudentGroup(String studentGroupId) {
        return scheduledClassRepository.findByStudentGroupId(studentGroupId);
    }

    // Validation method (optional - can be moved to GA service)
    // public boolean validateSchedule(List<ScheduledClass> schedule) {
    //     return geneticSchedulingService.validateSchedule(schedule);
    // }
}