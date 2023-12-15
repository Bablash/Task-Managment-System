package com.example.demo.error_message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerError {
    private String message;

    private String debugMessage;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ControllerError(String message, String debugMessage){
        this.message=message;
        this.debugMessage=debugMessage;
    }
}
