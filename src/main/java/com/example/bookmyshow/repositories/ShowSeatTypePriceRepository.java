package com.example.bookmyshow.repositories;

import com.example.bookmyshow.models.Show;
import com.example.bookmyshow.models.ShowSeatTypePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShowSeatTypePriceRepository extends JpaRepository<ShowSeatTypePrice, Long> {

    List<ShowSeatTypePrice> findAllByShow(Show show);
}
