package hhplus.tdd.concert.app.application.waiting.service;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingNumQuery;
import hhplus.tdd.concert.app.application.waiting.dto.WaitingTokenCommand;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.types.WaitingStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
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
    private final PaymentRepository paymentRepository;

    private final String WAITING_TOKEN_KEY = "waitingToken";
    private final String ACTIVE_TOKEN_KEY = "activeToken";

    /* 유저 대기열 생성 */
//    public WaitingTokenCommand enqueueMember(long memberId){
//        Member member = memberRepository.findByMemberId(memberId);
//        Member.checkMemberExistence(member);
//
//        Waiting existWaiting = waitingRepository.findByMemberAndStatusNot(member, WaitingStatus.EXPIRED);
//        Waiting waiting = Waiting.generateOrReturnWaitingToken(existWaiting, member);
//        waitingRepository.save(waiting);
//        return new WaitingTokenCommand(waiting.getToken());
//    }

    public WaitingTokenCommand enqueueMember(long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        Member.checkMemberExistence(member);

        Waiting existWaiting = waitingRepository.findByMemberAndStatusNot(member, WaitingStatus.EXPIRED);
        Waiting waiting = Waiting.generateOrReturnWaitingToken(existWaiting, member);
        waitingRepository.save(waiting);
        return new WaitingTokenCommand(waiting.getToken());
    }

    /* 유저 대기열 순번 조회 */
    public WaitingNumQuery loadWaiting(String waitingToken){
        Waiting waiting = waitingRepository.findByTokenOrThrow(waitingToken);

        int waitings = waitingRepository.countByWaitingIdLessThanAndStatus(waiting.getWaitingId(), WaitingStatus.STAND_BY);
        return new WaitingNumQuery(waitings);
    }

    /* 대기열 만료 */
    @Transactional
    public void expiredWaiting(){
        List<Waiting> waitings = waitingRepository.findByExpiredAtLessThan(LocalDateTime.now());
        for(Waiting waiting : waitings){
            Member member = waiting.getMember();
            Payment payment = paymentRepository.findByMember(member);
            ConcertSeat concertSeat = payment.getReservation().getSeat();
            Reservation reservation = payment.getReservation();

            // 결제 실패 처리
            concertSeat.open();
            reservation.cancel();
            waiting.stop();
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
                waiting.in();
            }
        }
    }

}
