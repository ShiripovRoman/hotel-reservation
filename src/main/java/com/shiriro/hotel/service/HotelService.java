package com.shiriro.hotel.service;

import com.shiriro.hotel.exception.HotelNotFoundException;
import com.shiriro.hotel.model.Hotel;
import com.shiriro.hotel.repository.HotelRepository;

public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public int countRoomsOfType(String hotelId, String roomType) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return (int) hotel.rooms().stream()
                .filter(room -> room.roomType().equals(roomType))
                .count();
    }

}
