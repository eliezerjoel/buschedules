package dev.eliezerjoelk.buschedules.dto;

// import javax.validation.constraints.NotBlank;

public class TimetableRequest {
    
    // @NotBlank(message = "Semester is required")
    private String semester;
    
    // @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    // Optional parameters for GA configuration
    private Integer populationSize;
    private Integer generations;
    private Double crossoverRate;
    private Double mutationRate;
    
    // Constructors
    public TimetableRequest() {}
    
    public TimetableRequest(String semester, String academicYear) {
        this.semester = semester;
        this.academicYear = academicYear;
    }
    
    // Getters and Setters
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    public Integer getPopulationSize() {
        return populationSize;
    }
    
    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }
    
    public Integer getGenerations() {
        return generations;
    }
    
    public void setGenerations(Integer generations) {
        this.generations = generations;
    }
    
    public Double getCrossoverRate() {
        return crossoverRate;
    }
    
    public void setCrossoverRate(Double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
    
    public Double getMutationRate() {
        return mutationRate;
    }
    
    public void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }
}