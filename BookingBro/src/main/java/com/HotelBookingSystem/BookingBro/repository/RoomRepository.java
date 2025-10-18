package com.HotelBookingSystem.BookingBro.repository;

import com.HotelBookingSystem.BookingBro.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // ✅ Fetch all rooms with their bookings in a single query
    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.bookings")
    List<Room> findAllWithBookings();

    // ✅ Paginated fetch with bookings
    @Query(value = "SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.bookings",
            countQuery = "SELECT COUNT(DISTINCT r) FROM Room r")
    Page<Room> findAllWithBookings(Pageable pageable);

    // ✅ Find room by ID with bookings
    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.bookings WHERE r.id = :id")
    Optional<Room> findByIdWithBookings(@Param("id") Long id);

    // ✅ Find available rooms (no overlapping bookings)
    @Query("SELECT DISTINCT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT b.room.id FROM BookedRoom b WHERE " +
            "(:checkIn < b.checkOutDate AND :checkOut > b.checkInDate))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);

    // ✅ Find available rooms with details
    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.bookings b WHERE r.id NOT IN " +
            "(SELECT br.room.id FROM BookedRoom br WHERE " +
            "(:checkIn < br.checkOutDate AND :checkOut > br.checkInDate))")
    List<Room> findAvailableRoomsWithDetails(@Param("checkIn") LocalDate checkIn,
                                             @Param("checkOut") LocalDate checkOut);

    // ✅ Find rooms by type
    @EntityGraph(attributePaths = {"bookings"})
    List<Room> findByRoomType(String roomType);

    // ✅ Find rooms by price range
    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.bookings WHERE r.roomPrice BETWEEN :minPrice AND :maxPrice")
    List<Room> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice);

    // ✅ Find rooms with active bookings
    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.bookings b WHERE b.checkOutDate >= :today")
    List<Room> findRoomsWithActiveBookings(@Param("today") LocalDate today);

    // ✅ Count available rooms for a date range
    @Query("SELECT COUNT(DISTINCT r) FROM Room r WHERE r.id NOT IN " +
            "(SELECT b.room.id FROM BookedRoom b WHERE " +
            "(:checkIn < b.checkOutDate AND :checkOut > b.checkInDate))")
    Long countAvailableRooms(@Param("checkIn") LocalDate checkIn,
                             @Param("checkOut") LocalDate checkOut);
}