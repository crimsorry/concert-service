package hhplus.tdd.concert.domain.entity.payment;

import hhplus.tdd.concert.domain.entity.concert.Reservation;
import hhplus.tdd.concert.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("결제 정보")
public class Payment {

    @Id
    @Comment("결제 ID")
    private Long payId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자 ID")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_id")
    @Comment("예약 ID")
    private Reservation reservation;

    @NotNull
    @Comment("결제 금액")
    @Column
    private Integer amount;

    @NotNull
    @Comment("결제 여부(true / false)")
    @Column
    private Boolean isPay;

    @NotNull
    @Comment("생성 시간")
    @Column
    private LocalDateTime createAt;
}