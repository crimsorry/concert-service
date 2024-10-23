package hhplus.tdd.concert.app.application.service.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.WaitingNumQuery;
import hhplus.tdd.concert.app.application.dto.waiting.WaitingTokenCommand;
import hhplus.tdd.concert.app.application.repository.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.concert.ConcertSeat;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.reservation.Reservation;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.app.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.common.types.ReserveStatus;
import hhplus.tdd.concert.common.types.SeatStatus;
import hhplus.tdd.concert.common.types.WaitingStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class WaitingService {

    private final int maxMember = 10;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;
    private final PaymentRepository paymentRepository;
    private final WaitingWrapRepository waitingWrapRepository;

    /* 유저 대기열 생성 */
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
        // 대기열 존재 여부 확인
        Waiting waiting = waitingWrapRepository.findByTokenOrThrow(waitingToken);

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
