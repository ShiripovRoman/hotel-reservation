package com.shiriro.hotel.cli;

import com.shiriro.hotel.model.StayPeriod;
import com.shiriro.hotel.service.AvailabilityService;
import com.shiriro.hotel.service.SearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ConsoleAppRunnerTest {

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private ConsoleAppRunner console;

    @Test
    void shouldReturnAvailabilityForSingleDate() {
        when(availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 1), "SGL"))
                .thenReturn(2);

        String result = console.handleCommand("Availability(H1, 20250901, SGL)");

        assertEquals("2", result);
    }

    @Test
    void shouldReturnAvailabilityForDateRange() {
        when(availabilityService.countAvailableRoomsInRange("H1",
                LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 3), "DBL"))
                .thenReturn(1);

        String result = console.handleCommand("Availability(H1, 20250901-20250903, DBL)");

        assertEquals("1", result);
    }

    @Test
    void shouldReturnSearchResults() {
        when(searchService.searchDateRanges("H1", 10, "SGL"))
                .thenReturn(List.of(
                        new StayPeriod(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 2), 2),
                        new StayPeriod(LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 3), 1)
                ));

        String result = console.handleCommand("Search(H1, 10, SGL)");

        assertEquals("(20250901-20250902, 2), (20250902-20250903, 1)", result);
    }

    @Test
    void shouldReturnEmptyLineWhenNoSearchResults() {
        when(searchService.searchDateRanges("H1", 10, "DBL")).thenReturn(List.of());

        String result = console.handleCommand("Search(H1, 10, DBL)");

        assertEquals("", result);
    }

    @Test
    void shouldThrowOnInvalidCommand() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                console.handleCommand("InvalidCommand(H1, 20250901, SGL)")
        );
        assertEquals("Unknown command", exception.getMessage());
    }
}
