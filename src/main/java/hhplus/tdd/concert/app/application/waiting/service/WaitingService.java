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
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

    private final int maxMember = 6000;
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
        Set<String> tokenSet = waitingRepository.getAllTokens(WAITING_TOKEN_KEY);

        if (tokenSet == null) {
            throw new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR);
        }

        String matchedValue = tokenSet.stream()
                .filter(obj -> obj.startsWith(waitingToken + ":"))
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
        List<ActiveToken> activeTokenList = waitingRepository.getActiveToken(ACTIVE_TOKEN_KEY);
        for(ActiveToken activeToken : activeTokenList){
            String[] sp = activeToken.token().split(":");
            Long expireTime = Long.parseLong(sp[2]);
            if(System.currentTimeMillis() > expireTime){
                Member member = memberRepository.findByMemberId(Long.parseLong(sp[1]));
                Payment payment = paymentRepository.findByMember(member);
                ConcertSeat concertSeat = payment.getReservation().getSeat();
                Reservation reservation = payment.getReservation();

                // 결제 실패 처리
                concertSeat.open();
                reservation.cancel();
                waitingRepository.deleteActiveToken(ACTIVE_TOKEN_KEY, activeToken.token());
            }
        }
    }

    /* 대기열 active 전환 */
    @Transactional
    public void activeWaiting(){
        // 현재 active 갯수 세기
        List<ActiveToken> waitingTokenList = waitingRepository.getWaitingTokenRange(WAITING_TOKEN_KEY, 0, maxMember-1);
        // maxMember 이하라면 : maxMember - 현재 active 수 만큼 위에서부터 active 전환
        for (ActiveToken activeToken : waitingTokenList) {
            String token = activeToken.token();
            waitingRepository.deleteWaitingToken(WAITING_TOKEN_KEY, token);
            waitingRepository.addActiveToken(ACTIVE_TOKEN_KEY, token + ":" + System.currentTimeMillis() + (5 * 60 * 1000));
        }
    }

}
