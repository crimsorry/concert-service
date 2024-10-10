package hhplus.tdd.concert.domain.entity.concert;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("콘서트 정보")
public class Concert {

    @Id
    @Comment("콘서트 ID")
    private Long concertId;

    @NotNull
    @Comment("콘서트 명")
    @Column(length = 255)
    private String concertTitle;

    @NotNull
    @Comment("콘서트 장소")
    @Column(length = 255)
    private String concertPlace;
}