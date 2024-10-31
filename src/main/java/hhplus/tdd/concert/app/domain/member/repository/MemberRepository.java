package hhplus.tdd.concert.app.domain.member.repository;

import hhplus.tdd.concert.app.domain.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberId(Long memberId);

//    @Lock(LockModeType.OPTIMISTIC)
    @Query("select m from Member m where m.memberId = :memberId")
    Member findByMemberIdWithOptimisticLock(Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.memberId = :memberId")
    Member findByMemberIdWithPessimisticLock(long memberId);

}
