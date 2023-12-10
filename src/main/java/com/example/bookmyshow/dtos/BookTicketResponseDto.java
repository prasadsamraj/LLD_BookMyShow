package com.example.bookmyshow.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookTicketResponseDto {
    private long bookingId;
    private ResponseStatus responseStatus;
    private String message;
}
