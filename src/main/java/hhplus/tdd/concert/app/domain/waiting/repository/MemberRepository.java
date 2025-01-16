package hhplus.tdd.concert.app.domain.waiting.repository;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;

import java.util.List;

public interface MemberRepository {

    Member save(Member member);

    List<Member> saveAll(List<Member> memberList);

    Member findByMemberId(Long memberId);

    Member findByMemberIdWithOptimisticLock(Long memberId);

    Member findByMemberIdWithPessimisticLock(long memberId);

}
