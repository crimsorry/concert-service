package hhplus.tdd.concert.app.application.waiting.service;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingNumDTO;
import hhplus.tdd.concert.app.application.waiting.dto.WaitingTokenDTO;
import hhplus.tdd.concert.app.domain.concert.entity.ConcertSeat;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.waiting.entity.Member;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;
import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.payment.repository.PaymentRepository;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import hhplus.tdd.concert.config.exception.FailException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

    private final int maxMember = 1000;
    private final int totalMaxMember = 24000;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;
    private final PaymentRepository paymentRepository;

    private final String WAITING_TOKEN_KEY = "waitingToken";
    private final String ACTIVE_TOKEN_KEY = "activeToken";

    /* 유저 대기열 생성 */
    public WaitingTokenDTO enqueueMember(long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        Member.checkMemberExistence(member);

        // uuid 생성
        String token = UUID.randomUUID().toString();

        // waiting token insert
        waitingRepository.addWaitingToken(WAITING_TOKEN_KEY, token + ":" + memberId, System.currentTimeMillis());

        return new WaitingTokenDTO(token);
    }

    public WaitingNumDTO loadWaiting(String waitingToken){
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

        return new WaitingNumDTO(userNum);
    }

    /* 대기열 만료 */
    @Transactional
    public void expiredWaiting(){
        List<ActiveToken> activeTokenList = waitingRepository.getActiveToken(ACTIVE_TOKEN_KEY);
        for(ActiveToken activeToken : activeTokenList){
            if(activeToken.getExpiredAt() != null){
                if(System.currentTimeMillis() > activeToken.getExpiredAt()){
                    Member member = memberRepository.findByMemberId(activeToken.getMemberId());
                    Payment payment = paymentRepository.findByMember(member);
                    ConcertSeat concertSeat = payment.getReservation().getSeat();
                    Reservation reservation = payment.getReservation();

                    // 결제 실패 처리
                    concertSeat.open();
                    reservation.cancel();
                    waitingRepository.deleteActiveToken(ACTIVE_TOKEN_KEY, activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + activeToken.getExpiredAt());
                }
            }

        }
    }

    /* 대기열 active 전환 */
    @Transactional
    public void activeWaiting(){
        // 현재 active 갯수 세기
        List<ActiveToken> waitingTokenList = waitingRepository.getWaitingTokenRange(WAITING_TOKEN_KEY, 0, maxMember-1);
        int activeTokenLen = waitingRepository.getActiveToken(ACTIVE_TOKEN_KEY).size();
        if(totalMaxMember>activeTokenLen) {
            // maxMember 이하라면 : maxMember - 현재 active 수 만큼 위에서부터 active 전환
            for (ActiveToken activeToken : waitingTokenList) {
                waitingRepository.deleteWaitingToken(WAITING_TOKEN_KEY, activeToken.getToken() + ":" + activeToken.getMemberId());
                waitingRepository.addActiveToken(ACTIVE_TOKEN_KEY, activeToken.getToken() + ":" + activeToken.getMemberId() + ":" + System.currentTimeMillis() + (5 * 60 * 1000));
            }
        }
    }

}
