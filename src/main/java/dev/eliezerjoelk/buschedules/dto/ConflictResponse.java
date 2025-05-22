package dev.eliezerjoelk.buschedules.dto;
import java.util.Map;

public class ConflictResponse {
    private boolean hasConflict;
    private Map<String, Object> conflictDetails;
    
    public ConflictResponse(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }
    
    public ConflictResponse(boolean hasConflict, Map<String, Object> conflictDetails) {
        this.hasConflict = hasConflict;
        this.conflictDetails = conflictDetails;
    }
    
    public boolean isHasConflict() {
        return hasConflict;
    }
    
    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }
    
    public Map<String, Object> getConflictDetails() {
        return conflictDetails;
    }
    
    public void setConflictDetails(Map<String, Object> conflictDetails) {
        this.conflictDetails = conflictDetails;
    }
}