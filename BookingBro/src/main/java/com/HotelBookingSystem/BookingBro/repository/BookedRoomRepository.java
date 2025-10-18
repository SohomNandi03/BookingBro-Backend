package com.HotelBookingSystem.BookingBro.repository;

import com.HotelBookingSystem.BookingBro.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {
    Optional<BookedRoom> findByBookingIdAndRoom_Id(Long bookingId, Long roomId);

    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByRoom_Id(Long roomId);

    @Query("SELECT b FROM BookedRoom b JOIN FETCH b.room WHERE b.bookingId = :bookingId")
    Optional<BookedRoom> findBookingWithRoom(@Param("bookingId") Long bookingId);

    @Query("SELECT b FROM BookedRoom b JOIN FETCH b.room")
    List<BookedRoom> findAllWithRoom();

    @Query("SELECT b FROM BookedRoom b WHERE b.room.id = :roomId " +
            "AND b.checkInDate < :checkOutDate " +
            "AND b.checkOutDate > :checkInDate")
    List<BookedRoom> findOverlappingBookings(@Param("roomId") Long roomId,
                                             @Param("checkInDate") LocalDate checkInDate,
                                             @Param("checkOutDate") LocalDate checkOutDate);
}

