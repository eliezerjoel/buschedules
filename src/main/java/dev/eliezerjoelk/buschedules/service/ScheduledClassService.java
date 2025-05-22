package dev.eliezerjoelk.buschedules.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.exception.SchedulingConflictException;
import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.ScheduledClass;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.repository.CourseRepository;
import dev.eliezerjoelk.buschedules.repository.InstructorRepository;
import dev.eliezerjoelk.buschedules.repository.ScheduledClassRepository;
import dev.eliezerjoelk.buschedules.repository.StudentGroupRepository;

@Service
public class ScheduledClassService {
    // Add logger for debugging
    private static final Logger logger = Logger.getLogger(ScheduledClassService.class.getName());

    @Autowired
    private ScheduledClassRepository scheduledClassRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Improved helper method to check if two time intervals overlap with better
    // logging
    private boolean doTimesOverlap(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
        // Log the time comparison for debugging
        logger.info("Comparing times: " + startTime1 + "-" + endTime1 + " vs " + startTime2 + "-" + endTime2);

        // An overlap occurs if the intervals (start1, end1) and (start2, end2)
        // intersect.
        // This is true if start1 is before end2 AND end1 is after start2.
        boolean overlap = startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);

        logger.info("Overlap result: " + overlap);
        return overlap;
    }

    public List<ScheduledClass> getAllScheduledClasses() {
        List<ScheduledClass> classes = scheduledClassRepository.findAll();
        logger.info("Retrieved " + classes.size() + " scheduled classes");
        return classes;
    }

    public Optional<ScheduledClass> getScheduledClassById(String id) {
        logger.info("Fetching scheduled class with ID: " + id);
        return scheduledClassRepository.findById(id);
    }

    // Original createScheduledClass method, now with conflict detection and
    // improved logging
    public ScheduledClass createScheduledClass(ScheduledClass scheduledClass) throws SchedulingConflictException {
        logger.info("Creating new scheduled class for instructor: " +
                (scheduledClass.getInstructor() != null ? scheduledClass.getInstructor().getFirstName() + " " +
                        scheduledClass.getInstructor().getLastName() : "null")
                +
                ", day: " + scheduledClass.getDayOfWeek());

        // Check for instructor conflict
        if (scheduledClass.getInstructor() != null) {
            List<ScheduledClass> instructorSchedules = scheduledClassRepository.findByInstructorAndDayOfWeek(
                    scheduledClass.getInstructor(), scheduledClass.getDayOfWeek());

            logger.info("Found " + instructorSchedules.size() + " existing schedules for this instructor on " +
                    scheduledClass.getDayOfWeek());

            for (ScheduledClass existingSchedule : instructorSchedules) {
                // Ensure we're not checking against itself if it's an update
                if (scheduledClass.getId() == null || !scheduledClass.getId().equals(existingSchedule.getId())) {
                    logger.info("Checking against existing schedule: " + existingSchedule.getId() +
                            " at " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime());

                    if (doTimesOverlap(scheduledClass.getStartTime(), scheduledClass.getEndTime(),
                            existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                        String errorMsg = String.format(
                                "Instructor %s is already scheduled for another class at this time on %s.",
                                scheduledClass.getInstructor().getFirstName() + " "
                                        + scheduledClass.getInstructor().getLastName(),
                                scheduledClass.getDayOfWeek());
                        logger.warning(errorMsg);
                        throw new SchedulingConflictException(errorMsg);
                    }
                }
            }
        } else {
            logger.warning("Attempting to create scheduled class with null instructor");
        }

        // Check for student group conflict
        if (scheduledClass.getStudentGroup() != null) {
            List<ScheduledClass> studentGroupSchedules = scheduledClassRepository.findByStudentGroupAndDayOfWeek(
                    scheduledClass.getStudentGroup(), scheduledClass.getDayOfWeek());

            logger.info("Found " + studentGroupSchedules.size() + " existing schedules for this student group on " +
                    scheduledClass.getDayOfWeek());

            for (ScheduledClass existingSchedule : studentGroupSchedules) {
                // Ensure we're not checking against itself if it's an update
                if (scheduledClass.getId() == null || !scheduledClass.getId().equals(existingSchedule.getId())) {
                    logger.info("Checking against existing schedule: " + existingSchedule.getId() +
                            " at " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime());

                    if (doTimesOverlap(scheduledClass.getStartTime(), scheduledClass.getEndTime(),
                            existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                        String errorMsg = String.format(
                                "Student group %s is already scheduled for another class at this time on %s.",
                                scheduledClass.getStudentGroup().getGroupName(),
                                scheduledClass.getDayOfWeek());
                        logger.warning(errorMsg);
                        throw new SchedulingConflictException(errorMsg);
                    }
                }
            }
        } else {
            logger.warning("Attempting to create scheduled class with null student group");
        }

        logger.info("No conflicts found, saving scheduled class");
        return scheduledClassRepository.save(scheduledClass);
    }

    // Original updateScheduledClass method, now with conflict detection and
    // improved logging
    public Optional<ScheduledClass> updateScheduledClass(String id, ScheduledClass updatedClass)
            throws SchedulingConflictException {
        logger.info("Updating scheduled class with ID: " + id);

        return scheduledClassRepository.findById(id)
                .map(existingClass -> {
                    // Check for instructor conflict (excluding the class being updated)
                    if (updatedClass.getInstructor() != null) {
                        List<ScheduledClass> instructorSchedules = scheduledClassRepository
                                .findByInstructorAndDayOfWeek(
                                        updatedClass.getInstructor(), updatedClass.getDayOfWeek());

                        logger.info(
                                "Found " + instructorSchedules.size() + " existing schedules for this instructor on " +
                                        updatedClass.getDayOfWeek());

                        for (ScheduledClass otherSchedule : instructorSchedules) {
                            if (!otherSchedule.getId().equals(id)) {
                                logger.info("Checking against existing schedule: " + otherSchedule.getId() +
                                        " at " + otherSchedule.getStartTime() + "-" + otherSchedule.getEndTime());

                                if (doTimesOverlap(updatedClass.getStartTime(), updatedClass.getEndTime(),
                                        otherSchedule.getStartTime(), otherSchedule.getEndTime())) {
                                    String errorMsg = String.format(
                                            "Instructor %s is already scheduled for another class at this time on %s.",
                                            updatedClass.getInstructor().getFirstName() + " "
                                                    + updatedClass.getInstructor().getLastName(),
                                            updatedClass.getDayOfWeek());
                                    logger.warning(errorMsg);
                                    throw new SchedulingConflictException(errorMsg);
                                }
                            }
                        }
                    }

                    // Check for student group conflict (excluding the class being updated)
                    if (updatedClass.getStudentGroup() != null) {
                        List<ScheduledClass> studentGroupSchedules = scheduledClassRepository
                                .findByStudentGroupAndDayOfWeek(
                                        updatedClass.getStudentGroup(), updatedClass.getDayOfWeek());

                        logger.info("Found " + studentGroupSchedules.size()
                                + " existing schedules for this student group on " +
                                updatedClass.getDayOfWeek());

                        for (ScheduledClass otherSchedule : studentGroupSchedules) {
                            if (!otherSchedule.getId().equals(id)) {
                                logger.info("Checking against existing schedule: " + otherSchedule.getId() +
                                        " at " + otherSchedule.getStartTime() + "-" + otherSchedule.getEndTime());

                                if (doTimesOverlap(updatedClass.getStartTime(), updatedClass.getEndTime(),
                                        otherSchedule.getStartTime(), otherSchedule.getEndTime())) {
                                    String errorMsg = String.format(
                                            "Student group %s is already scheduled for another class at this time on %s.",
                                            updatedClass.getStudentGroup().getGroupName(),
                                            updatedClass.getDayOfWeek());
                                    logger.warning(errorMsg);
                                    throw new SchedulingConflictException(errorMsg);
                                }
                            }
                        }
                    }

                    // Update fields from updatedClass
                    existingClass.setCourse(updatedClass.getCourse());
                    existingClass.setInstructor(updatedClass.getInstructor());
                    existingClass.setDayOfWeek(updatedClass.getDayOfWeek());
                    existingClass.setStartTime(updatedClass.getStartTime());
                    existingClass.setEndTime(updatedClass.getEndTime());
                    existingClass.setStudentGroup(updatedClass.getStudentGroup());
                    existingClass.setSemester(updatedClass.getSemester());
                    existingClass.setAcademicYear(updatedClass.getAcademicYear());

                    logger.info("No conflicts found, saving updated scheduled class");
                    return scheduledClassRepository.save(existingClass);
                });
    }

    public void deleteScheduledClass(String id) {
        logger.info("Deleting scheduled class with ID: " + id);
        scheduledClassRepository.deleteById(id);
    }

    /**
     * Fixed method for individual conflict checks.
     * This method will use existing conflict detection logic with improved logging.
     */
    public boolean hasScheduleConflict(String lecturerId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        logger.info("Checking conflicts for lecturer: " + lecturerId +
                ", day: " + dayOfWeek +
                ", time: " + startTime + "-" + endTime);

        // First, check if the instructor exists
        Optional<Instructor> instructorOptional = instructorRepository.findById(lecturerId);
        if (instructorOptional.isEmpty()) {
            logger.info("Instructor not found with ID: " + lecturerId + ", no conflict possible");
            return false; // No instructor, no conflict
        }

        Instructor instructor = instructorOptional.get();
        logger.info("Found instructor: " + instructor.getFirstName() + " " + instructor.getLastName());

        // Check for instructor conflict - FIXED: Ensure we're getting the right data
        List<ScheduledClass> instructorSchedules = scheduledClassRepository.findByInstructorAndDayOfWeek(instructor,
                dayOfWeek);

        // Log the number of scheduled classes found
        logger.info("Found " + instructorSchedules.size() + " scheduled classes for this instructor on " + dayOfWeek);

        // If no scheduled classes, return false immediately (no conflict)
        if (instructorSchedules.isEmpty()) {
            logger.info("No scheduled classes found for this instructor on this day, no conflict possible");
            return false;
        }

        // Check each scheduled class for time conflicts
        for (ScheduledClass existingSchedule : instructorSchedules) {
            logger.info("Checking against scheduled class: " +
                    "Course: "
                    + (existingSchedule.getCourse() != null ? existingSchedule.getCourse().getCourseName() : "null") +
                    ", Time: " + existingSchedule.getStartTime() + "-" + existingSchedule.getEndTime());

            if (doTimesOverlap(startTime, endTime, existingSchedule.getStartTime(), existingSchedule.getEndTime())) {
                logger.info("Conflict found! Times overlap with existing schedule");
                return true; // Conflict found
            }
        }

        logger.info("No conflicts found for this instructor at this time");
        return false; // No conflict found
    }

    /**
     * Get available time slots for a course-instructor combination
     * Fixed to use proper repository methods and improved logging
     */
    public List<TimeSlot> getAvailableTimeSlots(String courseId, String instructorId) {
        logger.info("Getting available time slots for course: " + courseId + ", instructor: " + instructorId);

        // Generate all possible time slots
        List<TimeSlot> allTimeSlots = generateAllPossibleTimeSlots();
        logger.info("Generated " + allTimeSlots.size() + " possible time slots");

        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            logger.warning("Instructor not found with ID: " + instructorId);
            return new ArrayList<>(); // Return empty list if instructor not found
        }

        Instructor instructor = instructorOptional.get();
        logger.info("Found instructor: " + instructor.getFirstName() + " " + instructor.getLastName());

        // Get all scheduled classes for this instructor
        // FIXED: Use a method that gets all scheduled classes for an instructor
        List<ScheduledClass> allInstructorSchedules = scheduledClassRepository.findByInstructor(instructor);
        logger.info("Found " + allInstructorSchedules.size() + " scheduled classes for this instructor");

        // Filter out time slots that conflict with the instructor's schedule
        List<TimeSlot> availableTimeSlots = allTimeSlots.stream()
                .filter(timeSlot -> {
                    boolean noConflict = !hasConflict(timeSlot, allInstructorSchedules);
                    if (!noConflict) {
                        logger.info("Time slot " + timeSlot.getDayOfWeek() + " " +
                                timeSlot.getStartTime() + "-" + timeSlot.getEndTime() +
                                " has a conflict");
                    }
                    return noConflict;
                })
                .collect(Collectors.toList());

        logger.info("Found " + availableTimeSlots.size() + " available time slots out of " +
                allTimeSlots.size() + " possible slots");

        return availableTimeSlots;
    }

    /**
     * Create a scheduled class with the given parameters, adapted for the /assign
     * endpoint.
     * This method will now throw SchedulingConflictException.
     */
    public ScheduledClass createScheduledClass(String courseId, String instructorId, String studentGroupId,
            DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
            String semester, String academicYear) throws SchedulingConflictException {
        logger.info("Creating scheduled class for course: " + courseId +
                ", instructor: " + instructorId +
                ", student group: " + studentGroupId +
                ", day: " + dayOfWeek +
                ", time: " + startTime + "-" + endTime);

        // Find the course, instructor, and student group
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.severe("Course not found with id: " + courseId);
                    return new RuntimeException("Course not found with id: " + courseId);
                });

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> {
                    logger.severe("Instructor not found with id: " + instructorId);
                    return new RuntimeException("Instructor not found with id: " + instructorId);
                });

        StudentGroup studentGroup = studentGroupRepository.findById(studentGroupId)
                .orElseThrow(() -> {
                    logger.severe("Student Group not found with id: " + studentGroupId);
                    return new RuntimeException("Student Group not found with id: " + studentGroupId);
                });

        // Create a temporary ScheduledClass object for conflict checking
        ScheduledClass newScheduledClass = new ScheduledClass();
        newScheduledClass.setCourse(course);
        newScheduledClass.setInstructor(instructor);
        newScheduledClass.setStudentGroup(studentGroup);
        newScheduledClass.setDayOfWeek(dayOfWeek);
        newScheduledClass.setStartTime(startTime);
        newScheduledClass.setEndTime(endTime);
        newScheduledClass.setSemester(semester);
        newScheduledClass.setAcademicYear(academicYear);

        // Use the existing createScheduledClass logic to perform conflict checks
        return createScheduledClass(newScheduledClass);
    }

    /**
     * Generate all possible time slots for scheduling
     * Improved with better logging
     */
    private List<TimeSlot> generateAllPossibleTimeSlots() {
        logger.info("Generating all possible time slots");

        List<TimeSlot> timeSlots = new ArrayList<>();
        // Create time slots for each day and hour based on your school's schedule
        // Example: Monday-Friday, 8 AM to 5 PM in 1-hour blocks
        DayOfWeek[] days = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY };

        for (DayOfWeek day : days) {
            for (int hour = 8; hour < 17; hour++) {
                LocalTime startTime = LocalTime.of(hour, 0);
                LocalTime endTime = LocalTime.of(hour + 1, 0);
                timeSlots.add(new TimeSlot(day, startTime, endTime));
            }
        }

        logger.info("Generated " + timeSlots.size() + " possible time slots");
        return timeSlots;
    }

    /**
     * Check if a time slot conflicts with existing schedule.
     * Fixed to properly handle DayOfWeek comparison and improved with logging
     */
    private boolean hasConflict(TimeSlot timeSlot, List<ScheduledClass> schedule) {
        if (schedule.isEmpty()) {
            logger.info("No scheduled classes to check against, no conflict possible");
            return false;
        }

        boolean hasConflict = schedule.stream().anyMatch(sc -> {
            // First check if days match
            boolean sameDayOfWeek = sc.getDayOfWeek() == timeSlot.getDayOfWeek();

            if (!sameDayOfWeek) {
                return false; // Different days, no conflict
            }

            // Then check if times overlap
            boolean timesOverlap = doTimesOverlap(
                    timeSlot.getStartTime(), timeSlot.getEndTime(),
                    sc.getStartTime(), sc.getEndTime());

            if (timesOverlap) {
                logger.info("Conflict found for time slot " + timeSlot.getDayOfWeek() + " " +
                        timeSlot.getStartTime() + "-" + timeSlot.getEndTime() +
                        " with scheduled class at " + sc.getStartTime() + "-" + sc.getEndTime());
            }

            return timesOverlap;
        });

        return hasConflict;
    }

    public List<ScheduledClass> findScheduledClassByCourse(String courseId) {

        return scheduledClassRepository.findByCourseId(courseId);
    }

    public List<ScheduledClass> findScheduledClassByInstructor(String instructorId) {

        return scheduledClassRepository.findByInstructorId(instructorId);
    }

    public List<ScheduledClass> findScheduledClassByStudentGroup(String studentGroupId) {

        return scheduledClassRepository.findByStudentGroupId(studentGroupId);
    }

}