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

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        String hotelsFile = "hotels.json";
        String bookingsFile = "bookings.json";

        ObjectMapper mapper = ObjectMapperFactory.getInstance();
        var hotelRepo = new FileBasedHotelRepository(hotelsFile, mapper);
        var bookingRepo = new FileBasedBookingRepository(bookingsFile, mapper);
        var hotelService = new HotelService(hotelRepo);
        var bookingService = new BookingService(bookingRepo);
        var availabilityService = new AvailabilityService(bookingService, hotelService);
        var searchService = new SearchService(bookingService, hotelService);

        new ConsoleAppRunner(availabilityService, searchService).run();
    }

}