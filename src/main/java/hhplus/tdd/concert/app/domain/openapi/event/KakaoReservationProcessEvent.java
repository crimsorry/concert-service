package hhplus.tdd.concert.app.domain.openapi.event;

import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoReservationProcessEvent {

    private ReservationDTO reservationDTO;

}
