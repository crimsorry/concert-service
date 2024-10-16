package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.waiting.QueueNumDto;
import hhplus.tdd.concert.application.dto.waiting.WaitingTokenDto;
import hhplus.tdd.concert.application.exception.FailException;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    /* 유저 대기열 생성 */
    public WaitingTokenDto enqueueMember(long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        if(member == null){
            throw new FailException("존재하지 않는 유저입니다.");
        }
        // TODO: 대기열 순번 관리 추리 및 동시성 처리.
        Waiting existWaiting = waitingRepository.findByMemberAndStatusNot(member, WaitingStatus.EXPIRED);
        Waiting waiting = Waiting.generateOrReturnWaitingToken(existWaiting, member);
        waitingRepository.save(waiting);
        return new WaitingTokenDto(waiting.getToken());
    }

    /* 유저 대기열 조회 */
    public QueueNumDto loadWaiting(String waitingToken){
        return new QueueNumDto(1);
    }



}
