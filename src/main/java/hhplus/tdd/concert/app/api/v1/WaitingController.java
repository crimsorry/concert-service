package hhplus.tdd.concert.app.api.v1;

import hhplus.tdd.concert.app.api.dto.response.ErrorRes;
import hhplus.tdd.concert.app.api.dto.response.waiting.WaitingNumRes;
import hhplus.tdd.concert.app.api.dto.response.waiting.WaitingTokenRes;
import hhplus.tdd.concert.app.application.dto.waiting.WaitingNumQuery;
import hhplus.tdd.concert.app.application.dto.waiting.WaitingTokenCommand;
import hhplus.tdd.concert.app.application.service.waiting.WaitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/{userId}/queue/token/issue")
    @Operation(summary = "유저 대기열 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaitingTokenRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<WaitingTokenRes> createUserQueue(
            @Schema(description = "유저 ID")
            @PathVariable("userId") long userId
    ){
        WaitingTokenCommand restResponse = waitingService.enqueueMember(userId);
        return new ResponseEntity<>(WaitingTokenRes.from(restResponse), HttpStatus.OK);
    }

    @GetMapping("/queue/token/issue/query")
    @Operation(summary = "유저 대기번호 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WaitingNumRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<WaitingNumRes> getUserQueueNum(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken
    ){
        WaitingNumQuery restResponse = waitingService.loadWaiting(waitingToken);
        return new ResponseEntity<>(WaitingNumRes.from(restResponse), HttpStatus.OK);
    }


}
