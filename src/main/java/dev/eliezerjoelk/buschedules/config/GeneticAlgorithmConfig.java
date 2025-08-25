package dev.eliezerjoelk.buschedules.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneticAlgorithmConfig {
    
    @Value("${ga.population.size:100}")
    private int populationSize;
    
    @Value("${ga.generations:500}")
    private int maxGenerations;
    
    @Value("${ga.crossover.rate:0.8}")
    private double crossoverRate;
    
    @Value("${ga.mutation.rate:0.1}")
    private double mutationRate;
    
    // Bean definitions for GA parameters
}