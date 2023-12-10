package com.example.bookmyshow.services;

import com.example.bookmyshow.models.*;
import com.example.bookmyshow.repositories.PaymentRepository;
import com.example.bookmyshow.repositories.ShowSeatTypePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Service
public class PaymentService {
    ShowSeatTypePriceRepository showSeatTypePriceRepository;
    PaymentRepository paymentRepository;
    @Autowired
    public PaymentService(ShowSeatTypePriceRepository showSeatTypePriceRepository, PaymentRepository paymentRepository) {
        this.showSeatTypePriceRepository = showSeatTypePriceRepository;
        this.paymentRepository = paymentRepository;
    }

    public long calculateAmount(Show show, List<ShowSeat> showSeats) {
        // Gold, platinum, silver
        List<ShowSeatTypePrice> showSeatTypePrices =showSeatTypePriceRepository.findAllByShow(show);
        long amount = 0;
        for(ShowSeat showSeat:showSeats){
            for(ShowSeatTypePrice showSeatTypePrice: showSeatTypePrices){
                if(showSeat.getSeat().getSeatType().equals(showSeatTypePrice.getSeatType())){
                    amount +=showSeatTypePrice.getPrice();
                }
            }
        }
        return amount;
    }
    public List<Payment> makePayment(Long amount){
        List<Payment> payments =new ArrayList<>();
        System.out.println("Make this Payment of Amount:"+amount);
        System.out.println("Please Confirm Payment is Done? Y/N : ");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        if(s.equalsIgnoreCase("Y")){
            Payment payment = new Payment();
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaymentMode(PaymentMode.CASH);
            payment.setRefNumber("NA");
            payment.setAmount(amount);
            payment = paymentRepository.save(payment);
            payments.add(payment);
        }else{
            System.out.println("Payment Failed : Booking UnSuccessful");
        }
        return payments;
    }
}
