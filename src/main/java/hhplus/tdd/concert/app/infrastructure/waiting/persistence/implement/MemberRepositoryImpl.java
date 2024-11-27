package hhplus.tdd.concert.app.infrastructure.waiting.persistence.implement;

import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.infrastructure.waiting.persistence.dataaccess.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository repository;

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public List<Member> saveAll(List<Member> memberList) {
        return repository.saveAll(memberList);
    }

    @Override
    public Member findByMemberId(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    @Override
    public Member findByMemberIdWithOptimisticLock(Long memberId) {
        return repository.findByMemberIdWithOptimisticLock(memberId);
    }

    @Override
    public Member findByMemberIdWithPessimisticLock(long memberId) {
        return repository.findByMemberIdWithPessimisticLock(memberId);
    }
}