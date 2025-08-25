package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.model.TimetableStatistics;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.TimetableRepository;
import dev.eliezerjoelk.buschedules.service.ga.GeneticAlgorithmService;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;

@Service
public class TimetableService {
    
    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private StudentGroupRepository studentGroupRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(TimetableService.class);
    
    public Timetable generateTimetable(String semester, String academicYear) {
        try {
            logger.info("Starting timetable generation for semester: {}, year: {}", semester, academicYear);
            
            // Fetch all required data
            List<Course> courses = courseRepository.findAll();
            List<Instructor> instructors = instructorRepository.findAll();
            List<StudentGroup> studentGroups = studentGroupRepository.findAll();
            List<TimeSlot> timeSlots = generateTimeSlots();
            
            // Validate data
            validateInputData(courses, instructors, studentGroups, timeSlots);
            
            // Generate optimal timetable using genetic algorithm
            Timetable generatedTimetable = geneticAlgorithmService.generateOptimalTimetable(
                courses, instructors, studentGroups, timeSlots);
            
            // Set semester and academic year for all scheduled classes
            generatedTimetable.getClasses().forEach(scheduledClass -> {
                scheduledClass.setSemester(semester);
                scheduledClass.setAcademicYear(academicYear);
            });
            
            // Save the generated timetable
            Timetable savedTimetable = timetableRepository.save(generatedTimetable);
            
            logger.info("Timetable generation completed successfully with {} classes", 
                       savedTimetable.getClasses().size());
            
            return savedTimetable;
            
        } catch (Exception e) {
            logger.error("Error generating timetable for semester: {}, year: {}", semester, academicYear, e);
            throw new RuntimeException("Failed to generate timetable: " + e.getMessage(), e);
        }
    }
    
    // public Timetable getTimetable(String semester, String academicYear) {
    //     return timetableRepository.findBySemesterAndAcademicYear(semester, academicYear)
    //         .orElseThrow(() -> new RuntimeException("Timetable not found for semester: " + semester + ", year: " + academicYear));
    // }
    
    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }
    
    // public void deleteTimetable(String semester, String academicYear) {
    //     Timetable timetable = getTimetable(semester, academicYear);
    //     timetableRepository.delete(timetable);
    //     logger.info("Deleted timetable for semester: {}, year: {}", semester, academicYear);
    // }
    
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
    
    private void validateInputData(List<Course> courses, List<Instructor> instructors, 
                                  List<StudentGroup> studentGroups, List<TimeSlot> timeSlots) {
        if (courses.isEmpty()) {
            throw new IllegalArgumentException("No courses found. Cannot generate timetable.");
        }
        
        if (instructors.isEmpty()) {
            throw new IllegalArgumentException("No instructors found. Cannot generate timetable.");
        }
        
        if (studentGroups.isEmpty()) {
            throw new IllegalArgumentException("No student groups found. Cannot generate timetable.");
        }
        
        if (timeSlots.isEmpty()) {
            throw new IllegalArgumentException("No time slots available. Cannot generate timetable.");
        }
        
        logger.info("Input validation passed: {} courses, {} instructors, {} student groups, {} time slots",
                   courses.size(), instructors.size(), studentGroups.size(), timeSlots.size());
    }
    
    // public TimetableStatistics getTimetableStatistics(String semester, String academicYear) {
    //     Timetable timetable = getTimetable(semester, academicYear);
        
    //     Map<String, Long> instructorWorkload = timetable.getClasses().stream()
    //         .collect(Collectors.groupingBy(
    //             sc -> sc.getInstructor().getFirstName() + " " + sc.getInstructor().getLastName(),
    //             Collectors.counting()));
        
    //     Map<DayOfWeek, Long> dailyClassCount = timetable.getClasses().stream()
    //         .collect(Collectors.groupingBy(
    //             sc -> sc.getTimeSlot().getDayOfWeek(),
    //             Collectors.counting()));
        
    //     return new TimetableStatistics(
    //         timetable.getClasses().size(),
    //         instructorWorkload,
    //         dailyClassCount
    //     );
    // }
}