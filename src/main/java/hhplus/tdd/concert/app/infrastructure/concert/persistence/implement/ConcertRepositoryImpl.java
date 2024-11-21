package hhplus.tdd.concert.app.infrastructure.concert.persistence.implement;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.infrastructure.concert.persistence.dataacess.jpa.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository repository;

    @Override
    public Concert save(Concert concert) {
        return repository.save(concert);
    }

    @Override
    public List<Concert> findAll() {
        return repository.findAll();
    }
}
