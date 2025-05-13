package com.shiriro.hotel.repository;

import com.shiriro.hotel.model.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> findAll();
}
