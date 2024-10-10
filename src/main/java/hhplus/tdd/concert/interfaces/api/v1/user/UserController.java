package hhplus.tdd.concert.interfaces.api.v1.user;

import hhplus.tdd.concert.application.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "모든 API 는 대기열 토큰 값이 필요합니다.")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PatchMapping("/charge")
    @Operation(summary = "잔액 충전")
    public ResponseEntity<?> updateUserCharge(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken,
            @Schema(description = "충전 금액")
            @RequestParam(required = true, defaultValue = "1") int amount
    ){
        userService.chargeAmount(queueToken, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/amount")
    @Operation(summary = "잔액 조회")
    public ResponseEntity<Integer> getUserAmount(
            @Parameter(hidden = true) @RequestHeader("queueToken") String queueToken
    ){
        int restResponse = userService.amount(queueToken);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }


}
