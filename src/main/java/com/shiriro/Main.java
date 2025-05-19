package com.shiriro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.cli.ConsoleAppRunner;
import com.shiriro.hotel.repository.FileBasedBookingRepository;
import com.shiriro.hotel.repository.FileBasedHotelRepository;
import com.shiriro.hotel.service.AvailabilityService;
import com.shiriro.hotel.service.BookingService;
import com.shiriro.hotel.service.HotelService;
import com.shiriro.hotel.service.SearchService;
import com.shiriro.hotel.util.ObjectMapperFactory;

import java.io.InputStream;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        String hotelsFile = null;
        String bookingsFile = null;

        for (int i = 0; i < args.length - 1; i++) {
            if ("--hotels".equals(args[i])) {
                hotelsFile = args[i + 1];
            } else if ("--bookings".equals(args[i])) {
                bookingsFile = args[i + 1];
            }
        }

        if (hotelsFile == null || bookingsFile == null) {
            System.err.println("ERROR: Missing required arguments.");
            System.err.println("Usage: java -jar app.jar --hotels path/to/hotels.json --bookings path/to/bookings.json");
            System.exit(1);
        }

        ObjectMapper mapper = ObjectMapperFactory.getInstance();
        var hotelRepo = new FileBasedHotelRepository(hotelsFile, mapper);
        var bookingRepo = new FileBasedBookingRepository(bookingsFile, mapper);
        var hotelService = new HotelService(hotelRepo);
        var bookingService = new BookingService(bookingRepo);
        var availabilityService = new AvailabilityService(bookingService, hotelService);
        var searchService = new SearchService(bookingService, hotelService);

        new ConsoleAppRunner(availabilityService, searchService).run();
    }


    // This alternative version loads JSON files form resources directory
    // It's especially useful when running the application from within an IDE,
    // as it avoids the need to manually pass command-line arguments.
//    public static void main(String[] args) {
//        ObjectMapper mapper = ObjectMapperFactory.getInstance();
//
//        String hotelsPath = Objects.requireNonNull(
//                Main.class.getClassLoader().getResource("hotels.json")).getFile();
//
//        String bookingsPath = Objects.requireNonNull(
//                Main.class.getClassLoader().getResource("bookings.json")).getFile();
//
//        var hotelRepo = new FileBasedHotelRepository(hotelsPath, mapper);
//        var bookingRepo = new FileBasedBookingRepository(bookingsPath, mapper);
//        var hotelService = new HotelService(hotelRepo);
//        var bookingService = new BookingService(bookingRepo);
//        var availabilityService = new AvailabilityService(bookingService, hotelService);
//        var searchService = new SearchService(bookingService, hotelService);
//
//        new ConsoleAppRunner(availabilityService, searchService).run();
//    }
}