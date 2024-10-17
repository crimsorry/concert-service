package hhplus.tdd.concert.interfaces.api.v1.waiting;

import hhplus.tdd.concert.application.dto.waiting.QueueNumDto;
import hhplus.tdd.concert.application.dto.waiting.WaitingTokenDto;
import hhplus.tdd.concert.application.service.waiting.WaitingService;
import hhplus.tdd.concert.interfaces.api.dto.response.userqueue.QueueNumRes;
import hhplus.tdd.concert.interfaces.api.dto.response.userqueue.WaitingTokenRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 토큰 API", description = "콘서트 대기열 발급 API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping("/{userId}/queue/token")
    @Operation(summary = "유저 대기열 토큰 발급")
    public ResponseEntity<WaitingTokenRes> createUserQueue(
            @Schema(description = "유저 ID")
            @PathVariable("userId") long userId
    ){
        WaitingTokenDto restResponse = waitingService.enqueueMember(userId);
        return new ResponseEntity<>(WaitingTokenRes.from(restResponse), HttpStatus.OK);
    }

    @GetMapping("/queue/token")
    @Operation(summary = "유저 대기번호 조회")
    public ResponseEntity<QueueNumRes> getUserQueueNum(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken
    ){
        QueueNumDto restResponse = waitingService.loadWaiting(waitingToken);
        return new ResponseEntity<>(QueueNumRes.from(restResponse), HttpStatus.OK);
    }


}
