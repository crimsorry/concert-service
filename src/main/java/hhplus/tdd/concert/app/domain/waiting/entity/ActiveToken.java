package hhplus.tdd.concert.app.domain.waiting.entity;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.config.exception.FailException;
import hhplus.tdd.concert.config.types.WaitingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Comment("Active Token")
public class ActiveToken {

    private String token;

    private Long memberId;

    private Long expiredAt;
}
