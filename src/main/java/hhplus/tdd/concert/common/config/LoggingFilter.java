package hhplus.tdd.concert.common.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class LoggingFilter implements Filter {

    public String getClientIP(HttpServletRequest request) {
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try{
            String method = ((HttpServletRequest)servletRequest).getMethod();
            String ip = getClientIP((HttpServletRequest)servletRequest);
            log.info("Request : {} uri=[{}] content-type=[{}] level=[INFO] clientIp=[{}]",
                    method,
                    ((HttpServletRequest)servletRequest).getRequestURI(),
                    servletRequest.getContentType(),
                    ip);
            ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
            filterChain.doFilter(servletRequest, responseCacheWrapperObject);
//            if(!method.equals("GET")){
                logResponse(responseCacheWrapperObject);
//            }
            responseCacheWrapperObject.copyBodyToResponse();
        }catch (Exception e) {
            MDC.clear(); // MDC 값 초기화 (스레드 풀 환경에서 안전)
        }
    }

    private void logResponse(ContentCachingResponseWrapper response){
        byte[] responseArray = response.getContentAsByteArray();
        if (responseArray.length > 0) {
            String responseStr = new String(responseArray);
            log.info("Payload : {} content-type=[{}] ", responseStr, response.getContentType());
        }
    }

}
