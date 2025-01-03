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
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("대기열")
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("대기열 ID")
    private Long waitingId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("사용자 ID")
    private Member member;

    @NotNull
    @Comment("토큰 값")
    @Column(length = 255)
    private String token;

    @NotNull
    @Comment("상태 값 (STAND_BY, ACTIVE, EXPIRED)")
    @Column
    @Enumerated(EnumType.STRING)
    private WaitingStatus status;

    @Comment("생성시간")
    @Column
    private LocalDateTime createAt;

    @Comment("만료시간")
    @Column
    private LocalDateTime expiredAt;

    public static void checkWaitingExistence(Waiting waiting){
        if(waiting == null){
            throw new FailException(ErrorCode.NOT_FOUND_WAITING_MEMBER, LogLevel.ERROR);
        }
    }

    public static void checkWaitingStatusActive(Waiting waiting) {
        if (waiting.getStatus() != WaitingStatus.ACTIVE) {
            throw new FailException(ErrorCode.WAITING_MEMBER, LogLevel.WARN);
        }
    }

    /* 대기열 발급 or 만료되지 않은 대기열 반환 로직 */
    public static Waiting generateOrReturnWaitingToken(Waiting waiting, Member member){
        // 대기열이 null 이거나 만료시간이 경과한 경우 스케줄러 텀이 1분으므로 만료에도 불구하고 stand_by 나 active 남은 경우 생김)
        if(waiting == null || waiting.getExpiredAt() != null && waiting.getExpiredAt().isBefore(LocalDateTime.now())){
            String waitingToken = generateWaitingToken();
            Waiting newWaiting = Waiting.builder()
                    .member(member)
                    .token(waitingToken)
                    .status(WaitingStatus.STAND_BY)
                    .createAt(LocalDateTime.now())
                    .build();
            return newWaiting;
        }
        return waiting;
    }

    /* 토큰 생성 */
    public static String generateWaitingToken(){
        return UUID.randomUUID().toString();
    }

    public void stop(){
        this.status = WaitingStatus.EXPIRED;
    }

    public void in(){
        this.status = WaitingStatus.ACTIVE;
    }

    public void limitPayTime(){
        this.expiredAt = LocalDateTime.now().plusMinutes(10);
    }


}