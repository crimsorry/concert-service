package hhplus.tdd.concert.app.interfaces.schedular.waiting;

import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingSchedule {

    private final WaitingService waitingService;

    /* 대기열 만료 스케줄러 */
    @Scheduled(cron = "*/10 * * * * *")
    public void expiredWaitingSchedule(){
        waitingService.expiredWaiting();
    }

    /* 대기열 active 전환 스케줄러 */
    @Scheduled(cron = "*/10 * * * * *")
    public void activeWaitingSchedule(){
        waitingService.activeWaiting();
    }

}
