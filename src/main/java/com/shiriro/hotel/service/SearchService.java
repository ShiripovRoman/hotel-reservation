package com.shiriro.hotel.service;

import com.shiriro.hotel.model.StayPeriod;
import com.shiriro.hotel.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final AvailabilityService availabilityService;

    public SearchService(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    public List<StayPeriod> searchDateRanges(String hotelId, int nightsAhead, String roomType) {
        LocalDate today = Dates.today();
        LocalDate endDate = today.plusDays(nightsAhead);

        List<StayPeriod> result = new ArrayList<>();

        LocalDate rangeStart = null;
        Integer currentAvailability = null;

        for (LocalDate date = today; date.isBefore(endDate); date = date.plusDays(1)) {
            int available = availabilityService.countAvailableRoomsOnDate(hotelId, date, roomType);

            if (available > 0) {
                if (rangeStart == null) {
                    rangeStart = date;
                    currentAvailability = available;
                } else if (!currentAvailability.equals(available)) {
                    result.add(new StayPeriod(rangeStart, date, currentAvailability));
                    rangeStart = date;
                    currentAvailability = available;
                }
            } else {
                if (rangeStart != null) {
                    result.add(new StayPeriod(rangeStart, date, currentAvailability));
                    rangeStart = null;
                    currentAvailability = null;
                }
            }
        }

        if (rangeStart != null) {
            result.add(new StayPeriod(rangeStart, endDate, currentAvailability));
        }

        return result;
    }
}

