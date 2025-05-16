package dev.eliezerjoelk.buschedules.service;

import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentGroupService {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    public List<StudentGroup> getAllStudentGroups() {
        return studentGroupRepository.findAll();
    }

    public Optional<StudentGroup> getStudentGroupById(String id) {
        return studentGroupRepository.findById(id);
    }

    public StudentGroup createStudentGroup(StudentGroup studentGroup) {
        return studentGroupRepository.save(studentGroup);
    }

    public Optional<StudentGroup> updateStudentGroup(String id, StudentGroup updatedGroup) {
        return studentGroupRepository.findById(id)
                .map(group -> {
                    group.setGroupName(updatedGroup.getGroupName());
                    group.setProgram(updatedGroup.getProgram());
                    group.setNumberOfStudents(updatedGroup.getNumberOfStudents());
                    return studentGroupRepository.save(group);
                });
    }

    public void deleteStudentGroup(String id) {
        studentGroupRepository.deleteById(id);
    }
}