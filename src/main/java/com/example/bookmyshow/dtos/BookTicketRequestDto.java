package com.example.bookmyshow.dtos;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BookTicketRequestDto {
    // show //
    private long userId;
    private long showId;
    private List<Long> showSeatIds;
}
