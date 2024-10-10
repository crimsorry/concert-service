package hhplus.tdd.concert.interfaces.api.v1.concert;

import hhplus.tdd.concert.application.dto.ConcertScheduleDto;
import hhplus.tdd.concert.application.dto.ConcertSeatDto;
import hhplus.tdd.concert.application.dto.PayDto;
import hhplus.tdd.concert.application.dto.ReservationDto;
import hhplus.tdd.concert.application.service.concert.ConcertService;
import hhplus.tdd.concert.interfaces.api.dto.request.ConcertReserveReq;
import hhplus.tdd.concert.interfaces.api.dto.response.ConcertScheduleRes;
import hhplus.tdd.concert.interfaces.api.dto.response.ConcertSeatRes;
import hhplus.tdd.concert.interfaces.api.dto.response.ReservationRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/concert")
@RequiredArgsConstructor
@Slf4j
public class ConcertController {

    private final ConcertService concertService;

    /* 예약 가능 날짜 조회 */
    @GetMapping("/date")
    public ResponseEntity<?> getConcertDate(
            @RequestHeader("queueToken") String queueToken
    ){
        List<ConcertScheduleDto> restResponse = concertService.loadConcertDate(queueToken);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertScheduleRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /* 예약 가능 좌석 조회 */
    @GetMapping("/seat")
    public ResponseEntity<?> getConcertSeat(
            @RequestHeader("queueToken") String queueToken,
            @RequestParam(required = true, defaultValue = "1") long scheduleId
    ){
        List<ConcertSeatDto> restResponse = concertService.loadConcertSeat(queueToken, scheduleId);
        return new ResponseEntity<>(restResponse.stream()
                .map(ConcertSeatRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /* 좌석 예약 요청 */
    @PostMapping("/reserve")
    public ResponseEntity<?> createConcertReserve(
            @RequestHeader("queueToken") String queueToken,
            @RequestBody ConcertReserveReq concertReserveReq
    ){
        PayDto restResponse =  concertService.processReserve(queueToken, concertReserveReq);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    /* 결제 처리 */
    @PatchMapping("/pay")
    public ResponseEntity<?> updateConcertPay(
            @RequestHeader("queueToken") String queueToken,
            @RequestParam(required = true, defaultValue = "1") long payId
    ){
        List<ReservationDto> restResponse = concertService.processPay(queueToken, payId);
        return new ResponseEntity<>(restResponse.stream()
                .map(ReservationRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

}
