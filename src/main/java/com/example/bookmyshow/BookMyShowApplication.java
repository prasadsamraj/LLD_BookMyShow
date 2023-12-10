package com.example.bookmyshow;

import com.example.bookmyshow.controllers.BookingController;
import com.example.bookmyshow.dtos.BookTicketRequestDto;
import com.example.bookmyshow.dtos.BookTicketResponseDto;
import com.example.bookmyshow.dtos.ResponseStatus;
import com.example.bookmyshow.models.ShowSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookMyShowApplication implements CommandLineRunner {
	@Autowired
	BookingController bookingController;
	public static void main(String[] args) {
		SpringApplication.run(BookMyShowApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		BookTicketRequestDto request = new BookTicketRequestDto();
		request.setShowId(1);
		List<Long> showSeats = List.of(1L,2L,3L);
		request.setShowSeatIds(showSeats);
		request.setUserId(1);
		BookTicketResponseDto response = bookingController.bookingTicket(request);
		if(response.getResponseStatus().equals(ResponseStatus.SUCCESS)){
			System.out.println("Booking is successful with below booking id");
			System.out.println(response.getBookingId());
		}else {
			System.out.println(response.getResponseStatus());
			System.out.println(response.getMessage());
		}
	}
}
