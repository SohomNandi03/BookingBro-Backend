package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.BookedRoom;
import java.util.List;
import java.util.Map;

public interface IBookedRoomService {

    // Get all bookings
    List<BookedRoom> getAllBookings();

    // Get room details by ID (returns a map)
    Map<Object, Object> getRoomById(Long id);

    // Cancel a booking by booking ID
    void cancelBooking(Long bookingId);

    // Get all bookings for a specific room
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    // Save a booking for a room
    String saveBooking(Long roomId, BookedRoom bookingRequest);

    // Find booking by confirmation code
    BookedRoom findByBookingConfirmationCode(String confirmationCode);


    List<BookedRoom> getAllBookingsWithRoom();
}
