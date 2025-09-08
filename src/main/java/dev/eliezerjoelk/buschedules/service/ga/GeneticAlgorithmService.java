package dev.eliezerjoelk.buschedules.service.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.eliezerjoelk.buschedules.model.Course;
import dev.eliezerjoelk.buschedules.model.Instructor;
import dev.eliezerjoelk.buschedules.model.StudentGroup;
import dev.eliezerjoelk.buschedules.model.TimeSlot;
import dev.eliezerjoelk.buschedules.model.Timetable;
import dev.eliezerjoelk.buschedules.service.ga.operators.CrossoverOperator;
import dev.eliezerjoelk.buschedules.service.ga.operators.MutationOperator;

// @Service
// public class GeneticAlgorithmService {

//     @Autowired
//     private FitnessEvaluator fitnessEvaluator;

//     @Autowired
//     private CrossoverOperator crossoverOperator;

//     @Autowired
//     private MutationOperator mutationOperator;

//     public Timetable generateOptimalTimetable(List<Course> courses, 
//                                             List<Instructor> instructors,
//                                             List<StudentGroup> studentGroups, 
//                                             List<TimeSlot> timeSlots) {

//         // GA Parameters
//         int populationSize = 100;
//         int generations = 500;
//         double crossoverRate = 0.8;
//         double mutationRate = 0.1;

//         // Initialize population
//         List<TimetableChromosome> population = initializePopulation(
//             populationSize, courses, instructors, studentGroups, timeSlots);

//         for (int generation = 0; generation < generations; generation++) {
//             // Evaluate fitness
//             evaluatePopulation(population);

//             // Selection, crossover, mutation
//             population = evolvePopulation(population, crossoverRate, mutationRate);

//             // Log progress
//             logGenerationStats(generation, population);
//         }

//         // Return best solution
//         TimetableChromosome best = getBestChromosome(population);
//         return convertToTimetable(best);
//     }
// }

@Service
public class GeneticAlgorithmService {

    @Autowired
    private FitnessEvaluator fitnessEvaluator;

    @Autowired
    private CrossoverOperator crossoverOperator;

    @Autowired
    private MutationOperator mutationOperator;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GeneticAlgorithmService.class);

    public Timetable generateOptimalTimetable(List<Course> courses,
            List<Instructor> instructors,
            List<StudentGroup> studentGroups,
            List<TimeSlot> timeSlots) {

        int populationSize = 100;
        int generations = 500;
        double crossoverRate = 0.8;
        double mutationRate = 0.1;

        List<TimetableChromosome> population = initializePopulation(
                populationSize, courses, instructors, studentGroups, timeSlots);

        for (int generation = 0; generation < generations; generation++) {
            evaluatePopulation(population);
            population = evolvePopulation(population, crossoverRate, mutationRate,
                    instructors, timeSlots);
            logGenerationStats(generation, population);
        }

        TimetableChromosome best = getBestChromosome(population);
        return convertToTimetable(best);
    }

    private List<TimetableChromosome> initializePopulation(int populationSize,
            List<Course> courses,
            List<Instructor> instructors,
            List<StudentGroup> studentGroups,
            List<TimeSlot> timeSlots) {
        List<TimetableChromosome> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            population.add(new TimetableChromosome(courses, instructors, studentGroups, timeSlots));
        }

        return population;
    }

    private void evaluatePopulation(List<TimetableChromosome> population) {
        for (TimetableChromosome chromosome : population) {
            double fitness = fitnessEvaluator.evaluateFitness(chromosome);
            chromosome.setFitness(fitness);
        }
    }

    private List<TimetableChromosome> evolvePopulation(List<TimetableChromosome> population,
            double crossoverRate, double mutationRate,
            List<Instructor> instructors,
            List<TimeSlot> timeSlots) {
        List<TimetableChromosome> newPopulation = new ArrayList<>();
        Random random = new Random();

        // Keep best individuals (elitism)
        population.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        int eliteCount = population.size() / 10;
        for (int i = 0; i < eliteCount; i++) {
            newPopulation.add(population.get(i));
        }

        // Generate offspring
        while (newPopulation.size() < population.size()) {
            TimetableChromosome parent1 = tournamentSelection(population);
            TimetableChromosome parent2 = tournamentSelection(population);

            TimetableChromosome offspring;
            if (random.nextDouble() < crossoverRate) {
                offspring = crossoverOperator.crossover(parent1, parent2);
            } else {
                offspring = parent1;
            }

            mutationOperator.mutate(offspring, mutationRate, instructors, timeSlots);
            newPopulation.add(offspring);
        }

        return newPopulation;
    }

    private TimetableChromosome tournamentSelection(List<TimetableChromosome> population) {
        Random random = new Random();
        int tournamentSize = 5;

        TimetableChromosome best = population.get(random.nextInt(population.size()));
        for (int i = 1; i < tournamentSize; i++) {
            TimetableChromosome candidate = population.get(random.nextInt(population.size()));
            if (candidate.getFitness() > best.getFitness()) {
                best = candidate;
            }
        }

        return best;
    }

    private void logGenerationStats(int generation, List<TimetableChromosome> population) {
        double bestFitness = population.stream()
                .mapToDouble(TimetableChromosome::getFitness)
                .max().orElse(0.0);

        double avgFitness = population.stream()
                .mapToDouble(TimetableChromosome::getFitness)
                .average().orElse(0.0);

        if (generation % 50 == 0) {
            logger.info("Generation {}: Best Fitness = {}, Average Fitness = {}");
        }

        // if (generation % 50 == 0) {
        // logger.info("Generation {}: Best Fitness = {}, Average Fitness = {}",
        // generation, bestFitness, avgFitness);
        // }
    }

    private TimetableChromosome getBestChromosome(List<TimetableChromosome> population) {
        return population.stream()
                .max(Comparator.comparing(TimetableChromosome::getFitness))
                .orElse(population.get(0));
    }

    private Timetable convertToTimetable(TimetableChromosome chromosome) {
        Timetable timetable = new Timetable();
        timetable.setClasses(new ArrayList<>(chromosome.getScheduledClasses()));
        return timetable;
    }
}
