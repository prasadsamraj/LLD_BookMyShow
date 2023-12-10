package com.example.bookmyshow.controllers;

import com.example.bookmyshow.dtos.BookTicketRequestDto;
import com.example.bookmyshow.dtos.BookTicketResponseDto;
import com.example.bookmyshow.dtos.ResponseStatus;
import com.example.bookmyshow.exceptions.InCompletePaymentException;
import com.example.bookmyshow.exceptions.SeatNotAvailableException;
import com.example.bookmyshow.exceptions.UserNotFoundException;
import com.example.bookmyshow.models.Booking;
import com.example.bookmyshow.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
@Controller
public class BookingController {
    BookingService bookingService;
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookTicketResponseDto bookingTicket(BookTicketRequestDto requestDto){
        long userId = requestDto.getUserId();
        long showId = requestDto.getShowId();
        List<Long> showSeats = requestDto.getShowSeatIds();
        BookTicketResponseDto bookTicketResponseDto =new BookTicketResponseDto();
        Booking booking;
        try{
            bookingService.blockSeats(showSeats) ;
            booking = bookingService.bookTicket(userId, showId, showSeats);
        }catch (UserNotFoundException | SeatNotAvailableException | InCompletePaymentException e) {
            bookTicketResponseDto.setMessage(e.getMessage());
            bookTicketResponseDto.setResponseStatus(ResponseStatus.FAILURE);
            return bookTicketResponseDto;
        }
        bookTicketResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        bookTicketResponseDto.setBookingId(booking.getId());
        bookTicketResponseDto.setMessage("Ticket is successfully booked.");
        return bookTicketResponseDto;
    }
}
