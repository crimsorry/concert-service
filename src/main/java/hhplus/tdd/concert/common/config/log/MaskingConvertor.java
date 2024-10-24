package hhplus.tdd.concert.common.config.log;

import org.springframework.stereotype.Component;

@Component
public class MaskingConvertor {

    private int maxLen = 500; // 원하는 최대 길이 설정

    public String masking(String message){
        message = message.replaceAll("(?<=\"memberName\":\")([^\"]{1})([^\"]+)([^\"]{1})(?=\")", "$1*$3");
        return message;
    }

    public String truncateResponse(String responseStr) {
        // 응답 데이터를 일정 길이로 자르고 필요하면 마스킹 처리
        return responseStr.length() > maxLen
                ? responseStr.substring(0, maxLen - 4) + "..." + "}"
                : responseStr;
    }

}
