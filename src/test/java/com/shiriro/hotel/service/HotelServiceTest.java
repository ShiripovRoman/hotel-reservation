package com.shiriro.hotel.service;

import com.shiriro.hotel.exception.HotelNotFoundException;
import com.shiriro.hotel.model.Hotel;
import com.shiriro.hotel.model.Room;
import com.shiriro.hotel.model.RoomType;
import com.shiriro.hotel.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void shouldReturnRoomCountForGivenType() {

        final String hotelId = "H1";
        final String roomType = "SGL";

        Hotel hotel = new Hotel(
                hotelId,
                "Test Hotel",
                List.of(new RoomType(roomType, "Single", List.of(), List.of())),
                List.of(
                        new Room("101", "SGL"),
                        new Room("102", "SGL"),
                        new Room("201", "DBL")
                )
        );
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        int result = hotelService.countRoomsOfType(hotelId, roomType);
        assertEquals(2, result);
    }

    @Test
    void shouldThrowWhenHotelNotFound() {
        when(hotelRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () ->
                hotelService.countRoomsOfType("UNKNOWN", "SGL")
        );
    }
}