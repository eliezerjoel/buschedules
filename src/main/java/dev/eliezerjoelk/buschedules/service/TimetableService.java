package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.TimetableRepository;
import dev.eliezerjoelk.buschedules.service.ga.GeneticAlgorithmService;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;
@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
private CourseRepository courseRepository;

@Autowired
private InstructorRepository instructorRepository;

@Autowired
private StudentGroupRepository studentGroupRepository;

    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;
    
public Timetable generateTimetable() {
    List<Course> courses = courseRepository.findAll();
    List<Instructor> instructors = instructorRepository.findAll();
    List<StudentGroup> studentGroups = studentGroupRepository.findAll();
    List<TimeSlot> timeSlots = generateTimeSlots(); // <-- use this instead of getAllTimeSlots()

    Timetable generatedTimetable = geneticAlgorithmService.generateOptimalTimetable(
        courses, instructors, studentGroups, timeSlots
    );
    generatedTimetable.setId("current");
    return timetableRepository.save(generatedTimetable);
}

private List<TimeSlot> generateTimeSlots() {
    List<TimeSlot> timeSlots = new ArrayList<>();

    // Generate time slots for Monday to Friday
    DayOfWeek[] weekdays = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};

    // Time slots from 8:00 AM to 6:00 PM (1-hour slots)
    LocalTime[] startTimes = {
        LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
        LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
        LocalTime.of(16, 0), LocalTime.of(17, 0)
    };

    for (DayOfWeek day : weekdays) {
        for (LocalTime startTime : startTimes) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDayOfWeek(day);
            timeSlot.setStartTime(startTime);
            timeSlot.setEndTime(startTime.plusHours(1)); // 1-hour duration
            timeSlots.add(timeSlot);
        }
    }

    return timeSlots;
}
    public Timetable getCurrentTimetable() {
        return timetableRepository.findById("current").orElse(null);
    }

    public void deleteCurrentTimetable() {
        timetableRepository.deleteById("current");
    }
}