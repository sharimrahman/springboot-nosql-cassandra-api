package com.t_mobile.cassandra_crud.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // 1. ResourceNotFoundException
    @Test
    void handleNotFoundTest() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals("Not Found", response.getBody().getError());
    }

    // 2. DataConflictException
    @Test
    void handleConflictTest() {
        DataConflictException ex = new DataConflictException("Duplicate user");

        ResponseEntity<ErrorResponse> response = handler.handleConflict(ex);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Duplicate user", response.getBody().getMessage());
        assertEquals("Conflict", response.getBody().getError());
    }

    // 3. Generic Exception
    @Test
    void handleGenericTest() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Something went wrong", response.getBody().getMessage());
        assertEquals("Internal Server Error", response.getBody().getError());
    }

    // 4. Validation Exception (@Valid failure)
    @Test
    void handleValidationTest() throws Exception {

        Object target = new Object();
        DataBinder binder = new DataBinder(target);
        BindingResult bindingResult = binder.getBindingResult();

        bindingResult.addError(new FieldError(
                "objectName",
                "field",
                "Field cannot be null"
        ));

        Method method = this.getClass().getDeclaredMethod("handleValidationTest");

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        null,
                        bindingResult
                );

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Field cannot be null", response.getBody().getMessage());
        assertEquals("Bad Request", response.getBody().getError());
    }

    // 5. Type Mismatch (id = "abc")
    @Test
    void handleTypeMismatchTest() {

        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "abc", Integer.class, "id", null, new RuntimeException()
                );

        ResponseEntity<ErrorResponse> response = handler.handleTypeMismatch(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid value for id", response.getBody().getMessage());
        assertEquals("Bad Request", response.getBody().getError());
    }
}