package com.HotelBookingSystem.BookingBro.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private Integer adults;
    private Integer children;
    private Integer totalGuest;
    private String bookingConfirmationCode;
    private LocalDateTime bookingDate;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private Long roomId;
    private String roomType;
    private BigDecimal roomPrice;
}