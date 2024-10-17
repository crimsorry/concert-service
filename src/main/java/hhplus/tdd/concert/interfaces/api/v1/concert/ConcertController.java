package hhplus.tdd.concert.interfaces.api.v1.concert;

import hhplus.tdd.concert.application.dto.concert.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.concert.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.payment.PayDto;
import hhplus.tdd.concert.application.service.concert.ConcertService;
import hhplus.tdd.concert.domain.exception.FailException;
import hhplus.tdd.concert.interfaces.api.dto.response.ErrorRes;
import hhplus.tdd.concert.interfaces.api.dto.response.concert.ConcertScheduleRes;
import hhplus.tdd.concert.interfaces.api.dto.response.concert.ConcertSeatRes;
import hhplus.tdd.concert.interfaces.api.dto.response.payment.PayRes;
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

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "콘서트 API", description = "모든 API 는 대기열 토큰 값이 필요합니다.")
@RestController
@RequestMapping("/api/v1/concert")
@RequiredArgsConstructor
@Slf4j
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/date")
    @Operation(summary = "예약 가능 날짜 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConcertScheduleRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<List<ConcertScheduleRes>> getConcertDate(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken
    ){
        List<ConcertScheduleDto> restResponse = concertService.loadConcertDate(waitingToken);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertScheduleRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/seat")
    @Operation(summary = "예약 가능 좌석 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConcertSeatRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<List<ConcertSeatRes>> getConcertSeat(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken,
            @Schema(description = "콘서트 스케줄 ID")
            @PathVariable("scheduleId") long scheduleId
    ){
        List<ConcertSeatDto> restResponse = concertService.loadConcertSeat(waitingToken, scheduleId);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertSeatRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/{seatId}/reserve")
    @Operation(summary = "좌석 예약 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PayRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<PayRes> createConcertReserve(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken,
            @Schema(description = "콘서트 좌석 ID")
            @PathVariable("seatId") long seatId
    ){
        PayDto restResponse = concertService.processReserve(waitingToken, seatId);
        return new ResponseEntity<>(PayRes.from(restResponse), HttpStatus.OK);
    }

}
