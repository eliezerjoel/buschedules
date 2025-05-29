package dev.eliezerjoelk.buschedules.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Program;
import dev.eliezerjoelk.buschedules.repository.ProgramRepository; // Import the repository
import dev.eliezerjoelk.buschedules.model.Department; // Import Department model for the relationship

@Service // Mark this class as a Spring Service component
public class ProgramService {

    @Autowired // Inject ProgramRepository
    private ProgramRepository programRepository;

    // Method to get all programs
    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    // Method to get a program by its ID
    public Optional<Program> getProgramById(String id) {
        return programRepository.findById(id);
    }

    // Method to create a new program
    public Program createProgram(Program program) {
        return programRepository.save(program);
    }

    // Method to update an existing program
    public Optional<Program> updateProgram(String id, Program updatedProgram) {
        return programRepository.findById(id)
                .map(program -> {
                    // Update the fields of the existing program
                    program.setName(updatedProgram.getName());
                    program.setShortCode(updatedProgram.getShortCode());
                    program.setCreditsRequired(updatedProgram.getCreditsRequired());
                    // Update the associated department if it's part of the update
                    // Make sure the updatedProgram's department has a valid ID if you're only sending IDs.
                    // If you're sending full Department objects, Spring Data MongoDB will handle @DBRef updates.
                    program.setDepartment(updatedProgram.getDepartment());
                    return programRepository.save(program); // Save the updated program
                });
    }

    // Method to delete a program by its ID
    public boolean deleteProgram(String id) {
        if (programRepository.existsById(id)) { // Check if the program exists
            programRepository.deleteById(id); // Delete by ID
            return true; // Indicate successful deletion
        }
        return false; // Indicate program not found
    }

    // Custom method to find programs by department (as discussed in frontend logic)
    // You'll need to define this method in your ProgramRepository interface.
    public List<Program> findByDepartmentId(String departmentId) {
        return programRepository.findByDepartmentId(departmentId);
    }
}