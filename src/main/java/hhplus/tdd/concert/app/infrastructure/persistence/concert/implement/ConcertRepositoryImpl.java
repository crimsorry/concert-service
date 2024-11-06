package hhplus.tdd.concert.app.infrastructure.persistence.concert.implement;

import hhplus.tdd.concert.app.domain.concert.entity.Concert;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.concert.dataaccess.jpa.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }
}
