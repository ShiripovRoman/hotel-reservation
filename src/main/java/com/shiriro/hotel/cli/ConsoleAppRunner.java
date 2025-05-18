package com.shiriro.hotel.cli;

import com.shiriro.hotel.model.StayPeriod;
import com.shiriro.hotel.service.AvailabilityService;
import com.shiriro.hotel.service.SearchService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleAppRunner {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final AvailabilityService availabilityService;
    private final SearchService searchService;

    public ConsoleAppRunner(AvailabilityService availabilityService, SearchService searchService) {
        this.availabilityService = availabilityService;
        this.searchService = searchService;
    }

    public void run() {

        System.out.println("Examples:");
        System.out.println("  Availability(H1, 20250901, SGL)");
        System.out.println("  Availability(H1, 20250901-20250903, DBL)");
        System.out.println("  Search(H1, 30, SGL)");
        System.out.println();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter command (blank to exit):");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) break;

                try {
                    String result = handleCommand(line);
                    System.out.println(result);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid command: " + e.getMessage());
                }
            }
        }
    }

    String handleCommand(String input) {
        if (input.startsWith("Availability(")) {
            return handleAvailability(input);
        } else if (input.startsWith("Search(")) {
            return handleSearch(input);
        } else {
            throw new IllegalArgumentException("Unknown command");
        }
    }

    private String handleAvailability(String input) {
        String content = input.substring("Availability(".length(), input.length() - 1);
        String[] parts = content.split(",");

        if (parts.length != 3)
            throw new IllegalArgumentException("Expected 3 arguments: hotelId, date or range, roomType");

        String hotelId = parts[0].trim();
        String dateStr = parts[1].trim();
        String roomType = parts[2].trim();

        if (dateStr.contains("-")) {
            String[] dates = dateStr.split("-");
            LocalDate from = LocalDate.parse(dates[0], DATE_TIME_FORMATTER);
            LocalDate to = LocalDate.parse(dates[1], DATE_TIME_FORMATTER);
            int count = availabilityService.countAvailableRoomsInRange(hotelId, from, to, roomType);
            return String.valueOf(count);
        } else {
            LocalDate date = LocalDate.parse(dateStr, DATE_TIME_FORMATTER);
            int count = availabilityService.countAvailableRoomsOnDate(hotelId, date, roomType);
            return String.valueOf(count);
        }
    }

    private String handleSearch(String input) {
        String content = input.substring("Search(".length(), input.length() - 1);
        String[] parts = content.split(",");

        if (parts.length != 3)
            throw new IllegalArgumentException("Expected 3 arguments: hotelId, nightsAhead, roomType");

        String hotelId = parts[0].trim();
        int nightsAhead = Integer.parseInt(parts[1].trim());
        String roomType = parts[2].trim();

        List<StayPeriod> results = searchService.searchDateRanges(hotelId, nightsAhead, roomType);
        if (results.isEmpty()) return "";

        return results.stream()
                .map(StayPeriod::toString)
                .collect(Collectors.joining(", "));
    }
}

