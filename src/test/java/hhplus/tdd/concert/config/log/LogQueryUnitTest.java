package hhplus.tdd.concert.config.log;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogQueryUnitTest {

    @InjectMocks
    private LogQuery logQuery;

    @Test
    public void 사용자_IP_조회() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // then & when
        String result = logQuery.loadClientIP(request);

        // 결과 검증
        assertEquals("127.0.0.1", result);
        assertEquals("127.0.0.1", MDC.get("clientIP"));
    }

    @Test
    public void 사용자_UUID_식별() {
        // then & when
        String result = logQuery.loadGenerateRequestId();

        // 결과 검증
        assertNotNull(result);
        assertEquals(result, MDC.get("requestId"));
    }
}