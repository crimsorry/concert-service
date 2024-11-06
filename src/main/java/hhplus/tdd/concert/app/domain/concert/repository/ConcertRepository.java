package hhplus.tdd.concert.app.domain.concert.repository;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ConcertRepository  {

    List<Concert> findAll();

}
