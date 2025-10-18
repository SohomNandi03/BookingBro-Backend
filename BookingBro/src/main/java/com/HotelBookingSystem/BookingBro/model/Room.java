package com.HotelBookingSystem.BookingBro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_price", nullable = false)
    private BigDecimal roomPrice;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;


    @Column(name = "booked", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean booked = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedRoom> bookings = new ArrayList<>();

    // Keep your code intact but implement addBooking properly
    public void addBooking(BookedRoom bookingRequest) {
        bookings.add(bookingRequest);
        bookingRequest.setRoom(this); // maintain bidirectional relationship
    }
}
