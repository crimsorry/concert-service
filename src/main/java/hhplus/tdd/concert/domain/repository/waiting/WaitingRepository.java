package hhplus.tdd.concert.domain.repository.waiting;

import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    Waiting findByMemberAndStatusNot(Member member, WaitingStatus status);
    List<Waiting> findByExpiredAtLessThan(LocalDateTime localDateTime);
    Waiting findByToken(String waitingToken);

}
