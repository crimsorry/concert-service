package hhplus.tdd.concert.interfaces.api.v1.user;

import hhplus.tdd.concert.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /* 잔액 충전 */
    @PatchMapping("/charge")
    public ResponseEntity<?> updateUserCharge(
            @RequestHeader("queueToken") String queueToken,
            @RequestParam(required = true, defaultValue = "1") int amount
    ){
        userService.chargeAmount(queueToken, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* 잔액 조회 */
    @GetMapping("/amount")
    public ResponseEntity<?> getUserAmount(
            @RequestHeader("queueToken") String queueToken
    ){
        int restResponse = userService.amount(queueToken);
        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }


}
