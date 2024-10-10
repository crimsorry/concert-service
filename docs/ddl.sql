-- concert

CREATE TABLE USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    user_name VARCHAR(13) NOT NULL COMMENT '사용자 명',
    charge INT NOT NULL default 0 COMMENT '잔액' 
) COMMENT = '사용자';

CREATE TABLE USER_QUEUE (
    queue_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL COMMENT '토큰 값',
    status VARCHAR(10) NOT NULL COMMENT '상태 값 (STAND_BY, ACTIVE, DONE, EXPIRED)',
    entered_at DATE NOT NULL COMMENT '진입시간',
    expired_at DATE COMMENT '만료시간',
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) COMMENT = '대기열';

-- index 추가
CREATE INDEX idx_user_queue_status ON USER_QUEUE(status);
CREATE INDEX idx_user_queue_entered_at ON USER_QUEUE(entered_at);

CREATE TABLE CONCERT (
    concert_id BIGINT PRIMARY KEY,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    concert_place VARCHAR(255) NOT NULL COMMENT '콘서트 장소'
) COMMENT = '콘서트 정보';

CREATE TABLE CONCERT_SCHEDULE (
    schedule_id BIGINT PRIMARY KEY,
    concert_id BIGINT NOT NULL,
    open_date DATE NOT NULL COMMENT '콘서트 개최 일',
    start_date DATE NOT NULL COMMENT '티켓 예매 시작 시간',
    end_date DATE NOT NULL COMMENT '티켓 예매 종료 시간',
    FOREIGN KEY (concert_id) REFERENCES CONCERT(concert_id)
) COMMENT = '콘서트 일정';

CREATE TABLE CONCERT_SEAT (
    seat_id BIGINT PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    seat_amount INT NOT NULL COMMENT '좌석 금액',
    seat_status VARCHAR(10) NOT NULL COMMENT '좌석 점유 여부 (STAND_BY, RESERVED, ASSIGN)',
    FOREIGN KEY (schedule_id) REFERENCES CONCERT_SCHEDULE(schedule_id)
) COMMENT = '콘서트 좌석';

-- index 추가
CREATE INDEX idx_concert_seat_schedule_id ON CONCERT_SEAT(seat_status);

CREATE TABLE RESERVATION (
    reserve_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    open_date DATE NOT NULL COMMENT '콘서트 개최 일',
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    seat_amount INT NOT NULL COMMENT '좌석 금액',
    reserve_status VARCHAR(10) NOT NULL COMMENT '예약 상태 (PENDING, RESERVED, CANCELED)',
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (schedule_id) REFERENCES CONCERT_SCHEDULE(schedule_id),
    FOREIGN KEY (seat_id) REFERENCES CONCERT_SEAT(seat_id)
) COMMENT = '예약 정보';


CREATE TABLE PAYMENT (
    pay_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reserve_id BIGINT NOT NULL,
    pay_amount INT NOT NULL COMMENT '결제 금액',
    is_pay BOOLEAN DEFAULT FALSE COMMENT '결제 여부(true / false)',
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (reserve_id) REFERENCES RESERVATION(reserve_id) 
) COMMENT = '결제 정보';



