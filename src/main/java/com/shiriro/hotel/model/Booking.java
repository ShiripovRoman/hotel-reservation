package com.shiriro.hotel.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record Booking(

        String hotelId,

        @JsonFormat(pattern = "yyyyMMdd")
        LocalDate arrival,

        @JsonFormat(pattern = "yyyyMMdd")
        LocalDate departure,

        String roomType,

        String roomRate

) {
}