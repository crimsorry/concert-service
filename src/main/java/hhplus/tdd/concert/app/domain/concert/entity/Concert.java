package hhplus.tdd.concert.app.domain.concert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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