package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.Room;
import com.HotelBookingSystem.BookingBro.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        // Handle optional file upload
        if (file != null && !file.isEmpty()) {
            room.setPhoto(file.getBytes());
        }

        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
        return room.get().getPhoto();
    }

    @Override
    public void deleteRoom(Long roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (roomType != null && !roomType.isEmpty()) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0) room.setPhoto(photoBytes);

        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findAll()
                .stream()
                .map(Room::getRoomType)
                .distinct()
                .toList();

    }

    @Override
    public List<Room> getAvailableRooms(String roomType, String checkInDateStr, String checkOutDateStr) {
        List<Room> rooms = roomRepository.findAll();

        // Filter by room type if provided
        if (roomType != null && !roomType.isEmpty()) {
            rooms = rooms.stream()
                    .filter(r -> r.getRoomType().equalsIgnoreCase(roomType))
                    .toList();
        }

        // Filter by availability if dates are provided
        if (checkInDateStr != null && checkOutDateStr != null) {
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);

            rooms = rooms.stream()
                    .filter(room -> room.getBookings().stream()
                            .noneMatch(booking ->
                                    // Check for overlap
                                    !(booking.getCheckOutDate().isBefore(checkInDate) ||
                                            booking.getCheckInDate().isAfter(checkOutDate))
                            )
                    ).toList();
        }

        return rooms;

    }


}
