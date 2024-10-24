package hhplus.tdd.concert.common.config.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Component
public class LogQuery {

    private final MaskingConvertor maskingConverter;

    public String loadClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        MDC.put("clientIP", ip);
        return ip;
    }

    public String loadGenerateRequestId(){
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        return requestId;
    }

    public void logResponse(ContentCachingResponseWrapper response) {
        byte[] responseArray = response.getContentAsByteArray();
        if (responseArray.length > 0) {
            String responseStr = new String(responseArray);
            String truncatedResponse = maskingConverter.truncateResponse(responseStr);
            truncatedResponse = maskingConverter.masking(truncatedResponse);
            log.info("Payload : {} content-type=[{}] ", truncatedResponse, response.getContentType());
        }
    }

}
