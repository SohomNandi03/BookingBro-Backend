package com.HotelBookingSystem.BookingBro.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BookingRequestDTO {

    @NotBlank(message = "Guest full name is required")
    private String guestFullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Guest email is required")
    private String guestEmail;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "Check-out date must be today or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @Min(value = 0, message = "Number of adults cannot be negative")
    private int numOfAdults;

    @Min(value = 0, message = "Number of children cannot be negative")
    private int numOfChildren;

    // totalNumOfGuest is removed, backend will calculate it

    // Getters & Setters
    public String getGuestFullName() {
        return guestFullName;
    }
    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }
    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }
    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }

    // Helper method to calculate total guests
    public int getTotalGuests() {
        return numOfAdults + numOfChildren;
    }
}
