package hhplus.tdd.concert.app.domain.entity.payment;

import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.FailException;
import hhplus.tdd.concert.common.types.PointType;
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
@Comment("포인트 내역 목록")
public class AmountHistory {

    @Id
    @Comment("포인트 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("사용자 ID")
    private Member member;

    @NotNull
    @Comment("포인트 금액")
    @Column
    private Integer amount;

    @NotNull
    @Comment("포인트 타입 (CHARGE, USE)")
    @Column
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @NotNull
    @Comment("생성 일")
    @Column
    private LocalDateTime createAt;

    public static AmountHistory generateAmountHistory(int amount, PointType pointType, Member member){
        AmountHistory amountHistory = AmountHistory
                .builder()
                .member(member)
                .amount(amount)
                .pointType(pointType)
                .createAt(LocalDateTime.now())
                .build();

        return amountHistory;
    }

    public static void checkAmountMinus(int amount){
        if(amount<0){
            throw new FailException(ErrorCode.AMNIOTIC_PAY);
        }
    }

}