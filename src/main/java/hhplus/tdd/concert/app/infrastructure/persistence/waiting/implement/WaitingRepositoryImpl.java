package hhplus.tdd.concert.app.infrastructure.persistence.waiting.implement;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.jpa.WaitingJpaRepository;
import hhplus.tdd.concert.config.types.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;

    @Override
    public Waiting save(Waiting waiting) {
        return waitingJpaRepository.save(waiting);
    }

    @Override
    public List<Waiting> saveAll(List<Waiting> waiting) {
        return waitingJpaRepository.saveAll(waiting);
    }

    @Override
    public List<Waiting> findAll() {
        return waitingJpaRepository.findAll();
    }

    @Override
    public Waiting findByWaitingId(Long waitingId) {
        return waitingJpaRepository.findByWaitingId(waitingId);
    }

    @Override
    public Waiting findByMemberAndStatusNot(Member member, WaitingStatus status) {
        return waitingJpaRepository.findByMemberAndStatusNot(member, status);
    }

    @Override
    public List<Waiting> findByExpiredAtLessThan(LocalDateTime localDateTime) {
        return waitingJpaRepository.findByExpiredAtLessThan(localDateTime);
    }

    @Override
    public int countByWaitingIdLessThanAndStatus(long waitingId, WaitingStatus statue) {
        return waitingJpaRepository.countByWaitingIdLessThanAndStatus(waitingId, statue);
    }

    @Override
    public List<Waiting> findByStatusOrderByWaitingId(WaitingStatus statue, Pageable pageable) {
        return waitingJpaRepository.findByStatusOrderByWaitingId(statue, pageable);
    }

    @Override
    public Waiting findByToken(String waitingToken) {
        return waitingJpaRepository.findByToken(waitingToken);
    }

    @Override
    public Waiting findByTokenOrThrow(String waitingToken) {
        return waitingJpaRepository.findByTokenOrThrow(waitingToken);
    }
}