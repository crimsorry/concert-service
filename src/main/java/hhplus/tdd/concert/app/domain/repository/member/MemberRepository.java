package hhplus.tdd.concert.app.domain.repository.member;

import hhplus.tdd.concert.app.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberId(Long memberId);

}
