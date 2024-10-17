package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.application.dto.waiting.QueueNumDto;
import hhplus.tdd.concert.application.dto.waiting.WaitingTokenDto;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final int maxMember = 10;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    /* 유저 대기열 생성 */
    public WaitingTokenDto enqueueMember(long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        Member.checkMemberExistence(member);

        Waiting existWaiting = waitingRepository.findByMemberAndStatusNot(member, WaitingStatus.EXPIRED);
        Waiting waiting = Waiting.generateOrReturnWaitingToken(existWaiting, member);
        waitingRepository.save(waiting);
        return new WaitingTokenDto(waiting.getToken());
    }

    /* 유저 대기열 순번 조회 */
    public QueueNumDto loadWaiting(String waitingToken){
        Waiting waiting = waitingRepository.findByToken(waitingToken);
        Waiting.checkWaitingExistence(waiting);

        int waitings = waitingRepository.countByWaitingIdLessThanAndStatus(waiting.getWaitingId(), WaitingStatus.STAND_BY);
        return new QueueNumDto(waitings);
    }

    /* 대기열 만료 */
    @Transactional
    public void expiredWaiting(){
        List<Waiting> waitings = waitingRepository.findByExpiredAtLessThan(LocalDateTime.now());
        for(Waiting waiting : waitings){
            waiting.setStatus(WaitingStatus.EXPIRED);
        }
    }

    /* 대기열 active 전환 */
    @Transactional
    public void activeWaiting(){
        // 현재 active 갯수 세기
        int activeWaiting = waitingRepository.findByStatusOrderByWaitingId(WaitingStatus.ACTIVE, PageRequest.of(0, maxMember)).size();
        // maxMember 이하라면 : maxMember - 현재 active 수 만큼 위에서부터 active 전환
        if(activeWaiting < maxMember){
            List<Waiting> waitings = waitingRepository.findByStatusOrderByWaitingId(WaitingStatus.STAND_BY, PageRequest.of(0, maxMember-activeWaiting));
            for(Waiting waiting : waitings){
                waiting.setStatus(WaitingStatus.ACTIVE);
                waiting.setExpiredAt(LocalDateTime.now().plusMinutes(5));
            }
        }
    }

}
