package dev.eliezerjoelk.buschedules.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    
    public ConflictException(String message) {
        super(message);
    }
    
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}

// @ResponseStatus(value = HttpStatus.NOT_FOUND)
// public class ResourceNotFoundException extends RuntimeException {
//     private static final long serialVersionUID = 1L;

//     public ResourceNotFoundException(String message) {
//         super(message);
//     }
// }

// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.ResponseStatus;