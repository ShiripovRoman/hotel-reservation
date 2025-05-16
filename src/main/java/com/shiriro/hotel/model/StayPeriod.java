package com.shiriro.hotel.model;

import java.time.LocalDate;

import static com.shiriro.hotel.util.Dates.toCompactDateString;

public record StayPeriod(LocalDate arrival, LocalDate departure, int count) {

    @Override
    public String toString() {
        return "(" + toCompactDateString(arrival) + "-" + toCompactDateString(departure) + ", " + count + ")";
    }

}
