package com.ra.advice;


import com.ra.model.dto.res.DataError;
import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
@RestControllerAdvice
public class APIValidateAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataError<Map<String,String>>> handleMethodArgument(MethodArgumentNotValidException ex){
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < ex.getFieldErrors().size(); i++) {
            FieldError error = ex.getFieldErrors().get(i);
            map.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new DataError<>("Error",map,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public DataError<String> handleNotSuch(NoSuchElementException e){
        return new DataError<>("Error",e.getLocalizedMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataException.class)
    public DataError<?> handUnData(DataException e){
        return new DataError<>("Error",e.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DataError<?>>  handleRuntimeException(RuntimeException e){
        return new ResponseEntity<>(new DataError<>("Error",e.getLocalizedMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
}