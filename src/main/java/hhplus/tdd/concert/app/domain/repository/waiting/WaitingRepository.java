package hhplus.tdd.concert.app.domain.repository.waiting;

import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.common.types.WaitingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    Waiting findByWaitingId(Long waitingId);
    Waiting findByMemberAndStatusNot(Member member, WaitingStatus status);
    List<Waiting> findByExpiredAtLessThan(LocalDateTime localDateTime);
    int countByWaitingIdLessThanAndStatus(long waitingId, WaitingStatus statue);
    List<Waiting> findByStatusOrderByWaitingId(WaitingStatus statue, Pageable pageable);
    Waiting findByToken(String waitingToken);

}
