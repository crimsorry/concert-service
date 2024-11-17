package hhplus.tdd.concert.app.application.openapi.service;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenapiService {

    public void processKakaoMsgReservation(ReservationDTO reservationDTO) {
        try {
            // 카카오톡 메시지 전송 로직
            log.info("Sending Kakao message for reservation: " + reservationDTO);
        } catch (Exception e) {
            // 실패 처리 로직
            log.warn("Failed to send Kakao message: " + e.getMessage());
        }
    }

    public void processKakaoMsgPay(PayDTO payDto) {
        try {
            // 카카오톡 메시지 전송 로직
            log.info("Sending Kakao message for reservation: " + payDto);
        } catch (Exception e) {
            // 실패 처리 로직
            log.warn("Failed to send Kakao message: " + e.getMessage());
        }
    }

}
