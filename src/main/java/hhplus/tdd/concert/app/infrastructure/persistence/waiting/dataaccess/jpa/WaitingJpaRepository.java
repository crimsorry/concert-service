package hhplus.tdd.concert.app.infrastructure.persistence.waiting.dataaccess.jpa;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.config.types.WaitingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    Waiting findByWaitingId(Long waitingId);

    Waiting findByMemberAndStatusNot(Member member, WaitingStatus status);

    List<Waiting> findByExpiredAtLessThan(LocalDateTime localDateTime);

    int countByWaitingIdLessThanAndStatus(long waitingId, WaitingStatus status);

    List<Waiting> findByStatusOrderByWaitingId(WaitingStatus status, Pageable pageable);

    Waiting findByToken(String waitingToken);

    default Waiting findByTokenOrThrow(String waitingToken) {
        Waiting waiting = findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);
        return waiting;
    }

}
