package hhplus.tdd.concert.domain.entity.member;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

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
}
