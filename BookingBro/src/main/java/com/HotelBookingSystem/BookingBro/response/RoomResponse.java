package com.HotelBookingSystem.BookingBro.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String photoBase64; // For frontend
    private boolean booked; // Optional

    // Minimal constructor for frontend
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    // Optional: constructor with photo
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.photoBase64 = photoBytes != null ? java.util.Base64.getEncoder().encodeToString(photoBytes) : null;
    }
}
