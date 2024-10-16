package hhplus.tdd.concert.interfaces.api.v1.payment;

import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;
import hhplus.tdd.concert.application.dto.concert.ReservationDto;
import hhplus.tdd.concert.application.dto.payment.UpdateChargeDto;
import hhplus.tdd.concert.application.service.PayService;
import hhplus.tdd.concert.interfaces.api.dto.response.payment.LoadAmountRes;
import hhplus.tdd.concert.interfaces.api.dto.response.concert.ReservationRes;
import hhplus.tdd.concert.interfaces.api.dto.response.payment.UpdateChargeRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "결제 API", description = "모든 API 는 대기열 토큰 값이 필요합니다.")
@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;

    @PatchMapping("")
    @Operation(summary = "잔액 충전")
    public ResponseEntity<UpdateChargeRes> updateCharge(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken,
            @Schema(description = "충전 금액")
            @RequestParam(required = true, defaultValue = "1") int amount
    ){
        UpdateChargeDto restResponse = payService.chargeAmount(queueToken, amount);
        return new ResponseEntity<>(UpdateChargeRes.from(restResponse), HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(summary = "잔액 조회")
    public ResponseEntity<LoadAmountRes> getAmount(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken
    ){
        LoadAmountDto restResponse = payService.loadAmount(queueToken);
        return new ResponseEntity<>(LoadAmountRes.from(restResponse), HttpStatus.OK);
    }

    @PatchMapping("/seat/{payId}")
    @Operation(summary = "결제 처리")
    public ResponseEntity<List<ReservationRes>> updateConcertPay(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken,
            @Schema(description = "결제 ID")
            @PathVariable("payId") long payId
    ){
        List<ReservationDto> restResponse = payService.processPay(queueToken, payId);
        return new ResponseEntity<>(restResponse.stream()
                .map(ReservationRes::from)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


}
