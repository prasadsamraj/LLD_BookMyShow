package com.example.bookmyshow.models;

import com.example.bookmyshow.BookMyShowApplication;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String name;
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}
