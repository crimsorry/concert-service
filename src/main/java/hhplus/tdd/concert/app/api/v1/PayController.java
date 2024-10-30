package hhplus.tdd.concert.app.api.v1;

import hhplus.tdd.concert.app.api.dto.response.ErrorRes;
import hhplus.tdd.concert.app.api.dto.response.reservation.ReservationRes;
import hhplus.tdd.concert.app.api.dto.response.payment.LoadAmountRes;
import hhplus.tdd.concert.app.api.dto.response.payment.UpdateChargeRes;
import hhplus.tdd.concert.app.application.dto.payment.LoadAmountQuery;
import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeCommand;
import hhplus.tdd.concert.app.application.dto.reservation.ReservationCommand;
import hhplus.tdd.concert.app.application.service.payment.PayService;
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

@Tag(name = "결제 API", description = "모든 API 는 대기열 토큰 값이 필요합니다.")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;

    @PatchMapping("/pay/charge")
    @Operation(summary = "잔액 충전")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateChargeRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<UpdateChargeRes> updateCharge(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken,
            @Schema(description = "충전 금액")
            @RequestParam(required = true, defaultValue = "1") int amount
    ){
        UpdateChargeCommand restResponse = payService.chargeAmount(waitingToken, amount);
//        UpdateChargeCommand restResponse = payService.chargeAmountOptimisticLock(waitingToken, amount);
        return new ResponseEntity<>(UpdateChargeRes.from(restResponse), HttpStatus.OK);
    }

    @GetMapping("/pay/query")
    @Operation(summary = "잔액 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoadAmountRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<LoadAmountRes> getAmount(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken
    ){
        LoadAmountQuery restResponse = payService.loadAmount(waitingToken);
        return new ResponseEntity<>(LoadAmountRes.from(restResponse), HttpStatus.OK);
    }

    @PatchMapping("/concerts/{concertId}/seats/{seatId}/pay/{payId}")
    @Operation(summary = "결제 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservationRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRes.class))),
    })
    public ResponseEntity<ReservationRes> updateConcertPay(
            @Parameter(hidden = true) @RequestHeader("waitingToken") String waitingToken,
            @Schema(description = "콘서트 ID")
            @PathVariable("concertId") long concertId,
            @Schema(description = "좌석 ID")
            @PathVariable("seatId") long seatId,
            @Schema(description = "결제 ID")
            @PathVariable("payId") long payId
    ){
        ReservationCommand restResponse = payService.processPay(waitingToken, payId);
        return new ResponseEntity<>(ReservationRes.fromCommand(restResponse), HttpStatus.OK);
    }


}
