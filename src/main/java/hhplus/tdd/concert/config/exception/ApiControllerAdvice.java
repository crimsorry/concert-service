package hhplus.tdd.concert.config.exception;

import hhplus.tdd.concert.app.interfaces.api.dto.response.ErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorRes> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new ErrorRes("500", "에러가 발생했습니다."));
    }

    @ExceptionHandler(FailException.class)
    public ResponseEntity<ErrorRes> handleCustomPointException(FailException e) {
        switch (e.getLogLevel()){
            case ERROR -> log.error("FailException : {}", e.getMessage(), e);
            case WARN -> log.warn("FailException : {}", e.getMessage(), e);
            default -> log.info("FailException : {}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRes("400", e.getMessage()));
    }


}