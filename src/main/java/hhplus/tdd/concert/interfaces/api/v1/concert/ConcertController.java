package hhplus.tdd.concert.interfaces.api.v1.concert;

import hhplus.tdd.concert.application.dto.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.PayDto;
import hhplus.tdd.concert.application.dto.ReservationDto;
import hhplus.tdd.concert.application.service.concert.ConcertService;
import hhplus.tdd.concert.interfaces.api.dto.request.ConcertReserveReq;
import hhplus.tdd.concert.interfaces.api.dto.response.ConcertScheduleRes;
import hhplus.tdd.concert.interfaces.api.dto.response.ConcertSeatRes;
import hhplus.tdd.concert.interfaces.api.dto.response.PayRes;
import hhplus.tdd.concert.interfaces.api.dto.response.ReservationRes;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<List<ConcertScheduleRes>> getConcertDate(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken
    ){
        List<ConcertScheduleDto> restResponse = concertService.loadConcertDate(queueToken);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertScheduleRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/seat")
    @Operation(summary = "예약 가능 좌석 조회")
    public ResponseEntity<List<ConcertSeatRes>> getConcertSeat(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken,
            @Schema(description = "콘서트 스케줄 ID")
            @RequestParam(required = true, defaultValue = "1") long scheduleId
    ){
        List<ConcertSeatDto> restResponse = concertService.loadConcertSeat(queueToken, scheduleId);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertSeatRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/reserve")
    @Operation(summary = "좌석 예약 요청")
    public ResponseEntity<PayRes> createConcertReserve(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken,
            @Schema(description = "콘서트 좌석 ID")
            @RequestParam(required = true, defaultValue = "1") long seatId
    ){
        PayDto restResponse = concertService.processReserve(queueToken, seatId);
        return new ResponseEntity<>(PayRes.from(restResponse), HttpStatus.OK);
    }

}
