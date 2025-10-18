package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.exception.InvalidBookingRequestException;
import com.HotelBookingSystem.BookingBro.exception.ResourceNotFoundException;
import com.HotelBookingSystem.BookingBro.model.BookedRoom;
import com.HotelBookingSystem.BookingBro.model.Room;
import com.HotelBookingSystem.BookingBro.repository.BookedRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookedRoomServiceImpl implements IBookedRoomService {

    private final BookedRoomRepository bookedRoomRepository;
    private final @Lazy IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookingsWithRoom() {
        return bookedRoomRepository.findAllWithRoom();
    }

    // ✅ MODIFIED: Now returns actual bookings WITH rooms (eagerly loaded) — fixes N+1
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookedRoomRepository.findAllWithRoom(); // ← Safe, efficient, prevents lazy load
    }

    @Override
    public Map<Object, Object> getRoomById(Long id) {
        Room room = roomService.getRoomById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        return Map.of(
                "id", room.getId(),
                "roomType", room.getRoomType(),
                "roomPrice", room.getRoomPrice(),
                "booked", room.isBooked()
        );
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookedRoomRepository.deleteById(bookingId);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookedRoomRepository.findByRoom_Id(roomId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckInDate() == null || bookingRequest.getCheckOutDate() == null) {
            throw new InvalidBookingRequestException("Check-in and Check-out dates must be provided");
        }

        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-out date cannot be before Check-in date");
        }

        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        List<BookedRoom> conflictingBookings = bookedRoomRepository.findOverlappingBookings(
                roomId,
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate()
        );

        if (!conflictingBookings.isEmpty()) {
            throw new InvalidBookingRequestException("Sorry, this room is not available for the selected dates.");
        }

        bookingRequest.setRoom(room);
        bookingRequest.setBookingConfirmationCode(UUID.randomUUID().toString());
        bookingRequest.calculateTotalGuests();

        bookedRoomRepository.save(bookingRequest);

        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookedRoomRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No booking found with booking code: " + confirmationCode));
    }

    // ✅ This method is unused now — but kept intact as requested
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()) &&
                        bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate())
        );
    }
}