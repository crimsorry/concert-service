package hhplus.tdd.concert.domain.entity.userqueue;

import hhplus.tdd.concert.domain.entity.user.User;
import hhplus.tdd.concert.domain.enums.UserQueueStatus;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("대기열")
public class UserQueue {

    @Id
    @Comment("대기열 ID")
    private Long queueId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자 ID")
    private User user;

    @NotNull
    @Comment("토큰 값")
    @Column(length = 255)
    private String token;

    @NotNull
    @Comment("상태 값 (STAND_BY, ACTIVE, EXPIRED)")
    @Column
    @Enumerated(EnumType.STRING)
    private UserQueueStatus status;

    @Comment("만료시간")
    @Column
    private LocalDateTime expiredAt;
}