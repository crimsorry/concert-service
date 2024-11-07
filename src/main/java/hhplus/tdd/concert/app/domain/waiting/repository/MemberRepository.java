package hhplus.tdd.concert.app.domain.waiting.repository;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MemberRepository {

    Member save(Member member);

    List<Member> saveAll(List<Member> memberList);

    Member findByMemberId(Long memberId);

    Member findByMemberIdWithOptimisticLock(Long memberId);

    Member findByMemberIdWithPessimisticLock(long memberId);

}
