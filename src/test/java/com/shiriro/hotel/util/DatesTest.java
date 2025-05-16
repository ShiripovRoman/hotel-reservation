package com.shiriro.hotel.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DatesTest {

    @Test
    void shouldFormatDateWithoutDashes() {
        LocalDate input = LocalDate.of(2025, 9, 1);
        String result = Dates.toCompactDateString(input);
        assertEquals("20250901", result);
    }

}