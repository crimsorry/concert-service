package hhplus.tdd.concert.app.application.waiting.service;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingNumQuery;
import hhplus.tdd.concert.app.application.waiting.dto.WaitingTokenCommand;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.member.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.member.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.model.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import hhplus.tdd.concert.config.types.WaitingStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public WaitingTokenCommand enqueueMember(long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        Member.checkMemberExistence(member);

        // uuid 생성
        String token = UUID.randomUUID().toString();

        // waiting token insert
        waitingRepository.addWaitingToken(WAITING_TOKEN_KEY, token + ":" + memberId, System.currentTimeMillis());

        return new WaitingTokenCommand(token);
    }

    public WaitingNumQuery loadWaiting(String waitingToken){
        Set<Object> tokenSet = waitingRepository.getAllTokens(WAITING_TOKEN_KEY);

        if (tokenSet == null) {
            throw new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR);
        }

        String matchedValue = tokenSet.stream()
                .filter(obj -> obj instanceof String && ((String) obj).startsWith(waitingToken + ":"))
                .map(Object::toString)
                .findFirst()
                .orElse(null);

        if(matchedValue == null) {
            throw new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR);
        }

        int memberId = Integer.parseInt(matchedValue.split(":")[1]);

        Long userNum = waitingRepository.getWaitingTokenScore(WAITING_TOKEN_KEY, waitingToken + ":" + memberId);

        return new WaitingNumQuery(userNum);
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
        Set<Object> waitingTokenList = waitingRepository.getWaitingTokenRange(WAITING_TOKEN_KEY, 0, maxMember-1);
        // maxMember 이하라면 : maxMember - 현재 active 수 만큼 위에서부터 active 전환
        for (Object token : waitingTokenList) {
            // waiting 삭제
            waitingRepository.deleteWaitingToken(WAITING_TOKEN_KEY, token);
            // active insert
            waitingRepository.addActiveToken(ACTIVE_TOKEN_KEY, token);
        }
    }

}
