package hhplus.tdd.concert.app.application.openapi.service;

import hhplus.tdd.concert.app.application.openapi.dto.KakaoMsgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenapiService {

    public void processKakaoMsg(KakaoMsgDto kakaoMsgDto) {
        try {
            // 카카오톡 메시지 전송 로직
            log.info("Sending Kakao message: " + kakaoMsgDto);
        } catch (Exception e) {
            // 실패 처리 로직
            log.warn("Failed to send Kakao message: " + e.getMessage());
        }
    }

}
