package com.shiriro.hotel.repository;

import com.shiriro.hotel.model.Hotel;

import java.util.List;

public interface HotelRepository {
    List<Hotel> findAll();
}
