package hhplus.tdd.concert.interfaces.api.v1.userqueue;

import hhplus.tdd.concert.application.service.userqueue.UserQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserQueueController {

    private final UserQueueService userQueueService;

    /* 유저 대기열 토큰 발급 */
    @PostMapping("/queue/token")
    public ResponseEntity<?> createUserQueue(
            @RequestParam(required = true, defaultValue = "1") long userId
    ){
        String restResponse = userQueueService.enqueueUser(userId);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }


}
