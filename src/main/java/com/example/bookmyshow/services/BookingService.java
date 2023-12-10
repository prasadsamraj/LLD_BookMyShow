package com.example.bookmyshow.services;

import com.example.bookmyshow.exceptions.InCompletePaymentException;
import com.example.bookmyshow.exceptions.SeatNotAvailableException;
import com.example.bookmyshow.exceptions.UserNotFoundException;
import com.example.bookmyshow.models.*;
import com.example.bookmyshow.repositories.BookingRepository;
import com.example.bookmyshow.repositories.ShowRepository;
import com.example.bookmyshow.repositories.ShowSeatRepository;
import com.example.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class BookingService {
    UserRepository userRepository;
    ShowRepository showRepository;
    ShowSeatRepository showSeatRepository;
    PaymentService paymentService;
    BookingRepository bookingRepository;
    @Autowired
    public BookingService(UserRepository userRepository, ShowRepository showRepository, ShowSeatRepository showSeatRepository, PaymentService paymentService, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.paymentService = paymentService;
        this.bookingRepository = bookingRepository;
    }

    public Booking bookTicket(long userId, long showId, List<Long> showSeats) throws UserNotFoundException, SeatNotAvailableException, InCompletePaymentException {
        // validate i/p 1)userId
        Optional<User> userOptional =userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("No user found the provided UserId");
        }
        User user = userOptional.get();
        Show show =showRepository.findById(showId).get();

//        List<ShowSeat> bookingSeats = blockSeats(showSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.IN_PROGRESS);
        booking.setShowSeats(showSeatRepository.findAllById(showSeats));
        booking.setShow(show);
        booking.setUser(user);
        booking.setAmount(paymentService.calculateAmount(show, booking.getShowSeats()));
        booking = bookingRepository.save(booking);
        List<Payment> payments = paymentService.makePayment(booking.getAmount());
        long paidAmount =0;
        for(Payment payment:payments){
            if(payment.getPaymentStatus().equals(PaymentStatus.SUCCESS))
                paidAmount += payment.getAmount();
        }
        if(paidAmount != booking.getAmount()){
            booking.setBookingStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);
            throw new InCompletePaymentException("Entire Amount is Not Recieved");
        }
        for (ShowSeat showSeat : booking.getShowSeats()){
            showSeat.setShowSeatStatus(ShowSeatStatus.BOOKED);
            showSeatRepository.save(showSeat);
        }
        booking.setBookingStatus(BookingStatus.BOOKED);
        return bookingRepository.save(booking);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void blockSeats(List<Long> showSeatIds) throws SeatNotAvailableException {
        List<ShowSeat> bookingSeats = showSeatRepository.findAllById(showSeatIds);
        for(ShowSeat showSeat:bookingSeats){
            if(showSeat.getShowSeatStatus().equals(ShowSeatStatus.BOOKED) ||
                    showSeat.getShowSeatStatus().equals(ShowSeatStatus.BLOCKED) &&
                            ((System.currentTimeMillis()-showSeat.getLastModifiedAt().getTime())/60000)<=10){
                throw new SeatNotAvailableException("Selected seats are not available at the moment");

            }
        }
        for(ShowSeat showSeat: bookingSeats) {
            showSeat.setShowSeatStatus(ShowSeatStatus.BLOCKED);
            showSeat.setLastModifiedAt(new Date());
            showSeatRepository.save(showSeat);
        }

    }

}
