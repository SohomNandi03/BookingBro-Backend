package com.HotelBookingSystem.BookingBro.controller;

import com.HotelBookingSystem.BookingBro.model.Room;
import com.HotelBookingSystem.BookingBro.response.RoomResponse;
import com.HotelBookingSystem.BookingBro.service.IRoomService;
import com.HotelBookingSystem.BookingBro.service.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;

    // Add new room
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addRoom(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
    ) throws IOException, SQLException {
        Room savedRoom = roomService.addNewRoom(file, roomType, roomPrice);
        RoomResponse response = new RoomResponse(
                savedRoom.getId(),
                savedRoom.getRoomType(),
                savedRoom.getRoomPrice()
        );
        if (savedRoom.getPhoto() != null && savedRoom.getPhoto().length > 0) {
            response.setPhotoBase64(java.util.Base64.getEncoder().encodeToString(savedRoom.getPhoto()));
        }
        return ResponseEntity.ok(response);
    }

    // Get all rooms (fixed to return RoomResponse)
    @GetMapping("/all")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = rooms.stream().map(room -> {
            RoomResponse rr = new RoomResponse(
                    room.getId(),
                    room.getRoomType(),
                    room.getRoomPrice()
            );
            if (room.getPhoto() != null && room.getPhoto().length > 0) {
                rr.setPhotoBase64(java.util.Base64.getEncoder().encodeToString(room.getPhoto()));
            }
            return rr;
        }).toList();

        return ResponseEntity.ok(roomResponses);
    }

    // Delete room by ID
    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update room
    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo) throws IOException {

        byte[] photoBytes = (photo != null && !photo.isEmpty())
                ? photo.getBytes()
                : roomService.getRoomPhotoByRoomId(roomId);

        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);

        RoomResponse roomResponse = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice()
        );
        if (theRoom.getPhoto() != null && theRoom.getPhoto().length > 0) {
            roomResponse.setPhotoBase64(java.util.Base64.getEncoder().encodeToString(theRoom.getPhoto()));
        }

        return ResponseEntity.ok(roomResponse);
    }

    // Get all room types
    @GetMapping("/types")
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    // Get room by ID
    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        RoomResponse response = theRoom
                .map(room -> {
                    RoomResponse rr = new RoomResponse(
                            room.getId(),
                            room.getRoomType(),
                            room.getRoomPrice()
                    );
                    if (room.getPhoto() != null && room.getPhoto().length > 0) {
                        rr.setPhotoBase64(java.util.Base64.getEncoder().encodeToString(room.getPhoto()));
                    }
                    return rr;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) String checkInDate,
            @RequestParam(required = false) String checkOutDate) {

        List<Room> availableRooms = roomService.getAvailableRooms(roomType, checkInDate, checkOutDate);

        List<RoomResponse> responseList = availableRooms.stream().map(room -> {
            RoomResponse rr = new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice());
            if (room.getPhoto() != null && room.getPhoto().length > 0) {
                rr.setPhotoBase64(java.util.Base64.getEncoder().encodeToString(room.getPhoto()));
            }
            return rr;
        }).toList();

        return ResponseEntity.ok(responseList);
    }



}
