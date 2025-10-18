package com.HotelBookingSystem.BookingBro.controller;

import com.HotelBookingSystem.BookingBro.DTO.BookingRequestDTO;
import com.HotelBookingSystem.BookingBro.exception.InvalidBookingRequestException;
import com.HotelBookingSystem.BookingBro.exception.ResourceNotFoundException;
import com.HotelBookingSystem.BookingBro.model.BookedRoom;
import com.HotelBookingSystem.BookingBro.model.Room;
import com.HotelBookingSystem.BookingBro.response.BookedRoomResponse;
import com.HotelBookingSystem.BookingBro.response.RoomResponse;
import com.HotelBookingSystem.BookingBro.service.IBookedRoomService;
import com.HotelBookingSystem.BookingBro.service.IRoomService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookedRoomController {

    private final IBookedRoomService bookingService;
    private final IRoomService roomService;

    public BookedRoomController(IBookedRoomService bookingService, IRoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookedRoomResponse>> getAllBookings() {
        List<BookedRoom> bookings = bookingService.getAllBookingsWithRoom();
        List<BookedRoomResponse> responses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            responses.add(getBookingResponse(booking));
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            return ResponseEntity.ok(getBookingResponse(booking));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(
            @PathVariable Long roomId,
            @Valid @RequestBody BookingRequestDTO bookingRequest) {

        try {
            BookedRoom bookedRoom = getBookedRoom(bookingRequest);
            String confirmationCode = bookingService.saveBooking(roomId, bookedRoom);

            // Fetch the saved booking and convert to response
            BookedRoom savedBooking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookedRoomResponse response = getBookingResponse(savedBooking);

            return ResponseEntity.ok(response);

        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private static @NotNull BookedRoom getBookedRoom(BookingRequestDTO bookingRequest) {
        BookedRoom booking = new BookedRoom();
        booking.setGuestFullName(bookingRequest.getGuestFullName());
        booking.setGuestEmail(bookingRequest.getGuestEmail());
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());
        booking.setNumOfAdults(bookingRequest.getNumOfAdults());
        booking.setNumOfChildren(bookingRequest.getNumOfChildren());
        // totalNumOfGuest is calculated separately
        return booking;
    }


    private BookedRoomResponse getBookingResponse(BookedRoom booking) {
        Room roomEntity = booking.getRoom();
        RoomResponse room = new RoomResponse(
                roomEntity.getId(),
                roomEntity.getRoomType(),
                roomEntity.getRoomPrice()
        );
        return new BookedRoomResponse(
                booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildren(),
                booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(),
                room
        );
    }
}
