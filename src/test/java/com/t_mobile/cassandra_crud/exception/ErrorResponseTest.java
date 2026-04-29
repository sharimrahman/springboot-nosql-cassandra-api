package com.t_mobile.cassandra_crud.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testAllGetters() {

        LocalDateTime now = LocalDateTime.now();

        ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                "User not found",
                now
        );

        // ALL getters call
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("User not found", error.getMessage());
        assertEquals(now, error.getTimestamp());
    }
}