package hhplus.tdd.concert.common.config;

import hhplus.tdd.concert.common.config.log.LogQuery;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class LoggingFilter implements Filter {

    private final LogQuery logQuery;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
            String requestId = logQuery.loadGenerateRequestId();
            MDC.put("requestId", requestId);
            String method = ((HttpServletRequest)servletRequest).getMethod();
            String ip = logQuery.loadClientIP((HttpServletRequest)servletRequest);
            log.info("Request : {} uri=[{}] content-type=[{}] level=[INFO] clientIp=[{}] requestId=[{}]",
                    method,
                    ((HttpServletRequest)servletRequest).getRequestURI(),
                    servletRequest.getContentType(),
                    ip,
                    requestId);
            ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
            filterChain.doFilter(servletRequest, responseCacheWrapperObject);
            if(responseCacheWrapperObject.getStatus() == 200){
                if(method.equals("GET") || method.equals("POST") || method.equals("PUT") || method.equals("PATCH") || method.equals("DELETE")){
                    logQuery.logResponse(responseCacheWrapperObject);
                }
            }
            responseCacheWrapperObject.copyBodyToResponse();
        }catch (Exception e) {
            log.error("Error in LoggingFilter : ", e);
        }finally {
            MDC.clear(); // MDC 값 초기화 (스레드 풀 환경에서 안전)
        }
    }

}
