package hhplus.tdd.concert.interfaces.api.v1.userqueue;

import hhplus.tdd.concert.application.service.userqueue.UserQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "대기열 토큰 API", description = "콘서트 대기열 발급 API")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserQueueController {

    private final UserQueueService userQueueService;

    @PostMapping("/queue/token")
    @Operation(summary = "유저 대기열 토큰 발급")
    public ResponseEntity<?> createUserQueue(
            @Schema(description = "유저 ID")
            @RequestParam(required = true, defaultValue = "1") long userId
    ){
        String restResponse = userQueueService.enqueueUser(userId);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }


}
