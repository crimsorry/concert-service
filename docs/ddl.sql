
CREATE TABLE USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    user_name VARCHAR(13) NOT NULL COMMENT '사용자 명',
    charge INT NOT NULL default 0 COMMENT '잔액' 
) COMMENT = '사용자';

CREATE TABLE USER_QUEUE (
    queue_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL COMMENT '토큰 값',
    status VARCHAR(10) NOT NULL COMMENT '상태 값',
    create_at DATETIME NOT NULL COMMENT '생성시간',
    expired_at DATETIME NOT NULL COMMENT '만료시간'
) COMMENT = '대기열';

CREATE TABLE CONCERT (
    concert_id BIGINT PRIMARY KEY,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    concert_place VARCHAR(255) NOT NULL COMMENT '콘서트 장소'
) COMMENT = '콘서트 정보';

CREATE TABLE CONCERT_SCHEDULE (
    schedule_id BIGINT PRIMARY KEY,
    concert_id BIGINT NOT NULL,
    open_date DATETIME NOT NULL COMMENT '콘서트 개최 일',
    start_date DATETIME NOT NULL COMMENT '티켓 예매 시작 시간',
    end_date DATETIME NOT NULL COMMENT '티켓 예매 종료 시간',
    capacity INT NOT NULL COMMENT '남은 좌석 수'
) COMMENT = '콘서트 일정';

CREATE TABLE CONCERT_SEAT (
    seat_id BIGINT PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    amount INT NOT NULL COMMENT '좌석 금액',
    seat_status VARCHAR(10) NOT NULL COMMENT '좌석 점유 여부'
) COMMENT = '콘서트 좌석';

CREATE TABLE RESERVATION (
    reserve_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    open_date DATETIME NOT NULL COMMENT '콘서트 개최 일',
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    amount INT NOT NULL COMMENT '좌석 금액',
    reserve_status VARCHAR(10) NOT NULL COMMENT '예약 상태'
) COMMENT = '예약 정보';


CREATE TABLE PAYMENT (
    pay_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reserve_id BIGINT NOT NULL,
    amount INT NOT NULL COMMENT '결제 금액',
    is_pay BOOLEAN DEFAULT FALSE COMMENT '결제 여부(true / false)',
    create_at DATETIME NOT NULL COMMENT '생성 시간'
) COMMENT = '결제 정보';

CREATE TABLE AMOUNT_HISTORY (
    point_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INT NOT NULL COMMENT '결제 금액',
    point_type VARCHAR(10) NOT NULL COMMENT '결제 타입 (CHARGE, USE)',
    create_at DATETIME NOT NULL COMMENT '생성 일'
) COMMENT = '금액 이력';
