package hhplus.tdd.concert.app.domain.concert.repository;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConcertRepository  {

    Concert save(Concert concert);

    List<Concert> findAll();

    List<Concert> getConcertList(Pageable pageable);

}
