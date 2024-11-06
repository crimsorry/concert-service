package hhplus.tdd.concert.app.domain.waiting.repository;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.config.types.WaitingStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface WaitingRepository {
    Waiting save(Waiting waiting);
    List<Waiting> saveAll(List<Waiting> waiting);
    List<Waiting> findAll();
    Waiting findByWaitingId(Long waitingId);
    Waiting findByMemberAndStatusNot(Member member, WaitingStatus status);
    List<Waiting> findByExpiredAtLessThan(LocalDateTime localDateTime);
    int countByWaitingIdLessThanAndStatus(long waitingId, WaitingStatus statue);
    List<Waiting> findByStatusOrderByWaitingId(WaitingStatus statue, Pageable pageable);
    Waiting findByToken(String waitingToken);
    Waiting findByTokenOrThrow(String waitingToken);
}