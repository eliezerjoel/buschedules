package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(String id) {
        return instructorRepository.findById(id);
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public Optional<Instructor> updateInstructor(String id, Instructor updatedInstructor) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    instructor.setFirstName(updatedInstructor.getFirstName());
                    instructor.setLastName(updatedInstructor.getLastName());
                    instructor.setEmail(updatedInstructor.getEmail());
                    instructor.setDepartment(updatedInstructor.getDepartment());
                    return instructorRepository.save(instructor);
                });
    }

    public void deleteInstructor(String id) {
        instructorRepository.deleteById(id);
    }
    
    public List<Instructor> getAvailableInstructorsForCourse() {
        return instructorRepository.findAll();
    }
    
}