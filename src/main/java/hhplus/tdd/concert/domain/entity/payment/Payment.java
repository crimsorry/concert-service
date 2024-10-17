package hhplus.tdd.concert.domain.entity.payment;

import hhplus.tdd.concert.domain.entity.concert.Reservation;
import hhplus.tdd.concert.domain.entity.member.Member;
import hhplus.tdd.concert.domain.exception.ErrorCode;
import hhplus.tdd.concert.domain.exception.FailException;
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
@Comment("결제 정보")
public class Payment {

    @Id
    @Comment("결제 ID")
    private Long payId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("사용자 ID")
    private Member member;

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

    public static void checkPaymentExistence(Payment payment){
        if(payment == null){
            throw new FailException(ErrorCode.NOT_FOUNT_PAYMENT);
        }
    }

    public static void checkPaymentStatue(Payment payment){
        if(payment.isPay){
            throw new FailException(ErrorCode.SUCCESS_PAY_SEAT);
        }
    }

    public static Payment generatePayment(Member member, Reservation reservation){
        Payment payment = Payment.builder()
                .member(member)
                .reservation(reservation)
                .amount(reservation.getAmount())
                .isPay(false)
                .createAt(LocalDateTime.now())
                .build();
        return payment;
    }



}