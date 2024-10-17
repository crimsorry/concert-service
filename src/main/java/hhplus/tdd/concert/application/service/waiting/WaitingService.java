package hhplus.tdd.concert.application.service.waiting;

import hhplus.tdd.concert.application.dto.waiting.QueueNumDto;
import hhplus.tdd.concert.application.dto.waiting.WaitingTokenDto;
import hhplus.tdd.concert.application.service.BaseService;
import hhplus.tdd.concert.domain.entity.concert.*;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.entity.payment.Payment;
import hhplus.tdd.concert.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.domain.entity.waiting.WaitingStatus;
import hhplus.tdd.concert.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.domain.repository.payment.PaymentRepository;
import hhplus.tdd.concert.domain.repository.waiting.WaitingRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaitingService extends BaseService {

    private final int maxMember = 10;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;
    private final PaymentRepository paymentRepository;

    public WaitingService(MemberRepository memberRepository, WaitingRepository waitingRepository, PaymentRepository paymentRepository) {
        super(waitingRepository);
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
        this.paymentRepository = paymentRepository;
    }

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
        Waiting waiting = findAndCheckWaiting(waitingToken);

        int waitings = waitingRepository.countByWaitingIdLessThanAndStatus(waiting.getWaitingId(), WaitingStatus.STAND_BY);
        return new QueueNumDto(waitings);
    }

    /* 대기열 만료 */
    @Transactional
    public void expiredWaiting(){
        List<Waiting> waitings = waitingRepository.findByExpiredAtLessThan(LocalDateTime.now());
        for(Waiting waiting : waitings){
            waiting.setStatus(WaitingStatus.EXPIRED);
            Member member = waiting.getMember();
            Payment payment = paymentRepository.findByMember(member);
            ConcertSeat concertSeat = payment.getReservation().getSeat();
            Reservation reservation = payment.getReservation();
            ConcertSchedule concertSchedule = concertSeat.getSchedule();

            // 결제 실패 처리
            concertSeat.setSeatStatus(SeatStatus.STAND_BY);
            reservation.setReserveStatus(ReserveStatus.CANCELED);
            concertSchedule.setCapacity(concertSchedule.getCapacity()+1);
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
            }
        }
    }

}
