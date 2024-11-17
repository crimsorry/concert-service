package hhplus.tdd.concert.app.infrastructure.event.waiting;

import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.waiting.event.WaitingExpiredEventListener;
import hhplus.tdd.concert.app.domain.waiting.entity.ActiveToken;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class WaitingExpiredEventListenerImpl implements WaitingExpiredEventListener {

    private final WaitingService waitingService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredEvent(ActiveToken activeToken) { // 전달 . domain 감! application 이나 도메인
        waitingService.deleteActiveToken(activeToken);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleWaitingExpiredTimeEvent(String value) {
        waitingService.updateActiveToken(value);
    }

    //저는 XXEventPublisher 인터페이스를 domain, XXSpringEventPublisher 를 infra ( 구체적 구현 ),
    // XXEventListener 를 interfaces/event 로 두는 편입니다.
    // ( 스프링 이벤트 리스너나 카프카 컨슈머가 될 텐데, application 의 UC 를 활용할 수 있도록 계층 간의 위계를 통일시키기 위함.
    //-- 그냥 제 의견임 --
    //* 저는 Listener 도 동작 별로 연관도에 따라서 나누는 편
}