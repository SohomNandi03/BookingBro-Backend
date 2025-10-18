package com.HotelBookingSystem.BookingBro.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private Boolean isBooked;
    private String description;
    private Integer maxOccupancy;
    private String photoBase64;
    private List<BookingDTO> bookings;
    private Boolean available;
}