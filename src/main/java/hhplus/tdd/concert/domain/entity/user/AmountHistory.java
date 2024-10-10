package hhplus.tdd.concert.domain.entity.user;

import hhplus.tdd.concert.domain.enums.PointType;
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
    @JoinColumn(name = "user_id")
    @Comment("사용자 ID")
    private User user;

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
}