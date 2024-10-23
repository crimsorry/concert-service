package hhplus.tdd.concert.common.config.exception;

import hhplus.tdd.concert.app.api.dto.response.ErrorRes;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorRes> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new ErrorRes("500", "에러가 발생했습니다."));
    }

    @ExceptionHandler(FailException.class)
    public ResponseEntity<ErrorRes> handleCustomPointException(FailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRes("400", e.getMessage()));
    }


}