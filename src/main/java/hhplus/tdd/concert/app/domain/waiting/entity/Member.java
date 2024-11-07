package hhplus.tdd.concert.app.domain.waiting.entity;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.config.exception.FailException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;
import org.springframework.boot.logging.LogLevel;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("사용자")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("사용자 ID")
    private Long memberId;

    @NotNull
    @Comment("사용자 명")
    @Column(length = 13)
    private String memberName;

    @NotNull
    @Comment("잔액")
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer charge;

//    @Version
//    private Integer version;

    public static void checkMemberExistence(Member member){
        if(member == null){
            throw new FailException(ErrorCode.NOT_FOUNT_MEMBER, LogLevel.ERROR);
        }
    }

    public static void checkMemberCharge(Member member, int amount){
        if(member.getCharge() + amount > 10000000){
            throw new FailException(ErrorCode.FULL_PAY, LogLevel.INFO);
        }
    }

    public static void checkMemberChargeLess(Member member, int amount){
        if(member.getCharge() < amount){
            throw new FailException(ErrorCode.EMPTY_PAY, LogLevel.INFO);
        }
    }

    public void charge(Integer amount){
        this.charge = this.charge + amount;
    }

    public void withdraw(Integer amount){
        this.charge = this.charge - amount;
    }

}
