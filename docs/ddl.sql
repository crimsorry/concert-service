-- FK 연관관계를 설정할 때, 개발자가 의도하지 않은 deadlock이나 cascade 관련 문제가 발생할 수 있습니다. 이러한 이유로 이번 프로젝트에서는 JPA 엔터티에서 연관관계를 직접 관리하고, DDL 작성 시 FK 제약 조건을 삭제하여 데이터베이스 제약 대신 JPA의 연관관계 관리 기능을 활용할 계획입니다.

CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    member_name VARCHAR(13) NOT NULL COMMENT '사용자 명',
    charge INT NOT NULL default 0 COMMENT '잔액' 
) COMMENT = '사용자';

CREATE TABLE waiting (
    waiting_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL COMMENT '토큰 값',
    status VARCHAR(10) NOT NULL COMMENT '상태 값',
    create_at DATETIME NOT NULL COMMENT '생성시간',
    expired_at DATETIME COMMENT '만료시간'
) COMMENT = '대기열';

CREATE TABLE concert (
    concert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    concert_place VARCHAR(255) NOT NULL COMMENT '콘서트 장소'
) COMMENT = '콘서트 정보';

CREATE TABLE concert_schedule (
    schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    concert_id BIGINT NOT NULL,
    open_date DATETIME NOT NULL COMMENT '콘서트 개최 일',
    start_date DATETIME NOT NULL COMMENT '티켓 예매 시작 시간',
    end_date DATETIME NOT NULL COMMENT '티켓 예매 종료 시간',
    capacity INT NOT NULL COMMENT '남은 좌석 수'
) COMMENT = '콘서트 일정';

CREATE TABLE concert_seat (
    seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    amount INT NOT NULL COMMENT '좌석 금액',
    seat_status VARCHAR(10) NOT NULL COMMENT '좌석 점유 여부'
) COMMENT = '콘서트 좌석';

CREATE TABLE reservation (
    reserve_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    concert_title VARCHAR(255) NOT NULL COMMENT '콘서트 명',
    open_date DATETIME NOT NULL COMMENT '콘서트 개최 일',
    seat_num VARCHAR(3) NOT NULL COMMENT '좌석 번호',
    amount INT NOT NULL COMMENT '좌석 금액',
    reserve_status VARCHAR(10) NOT NULL COMMENT '예약 상태'
) COMMENT = '예약 정보';


CREATE TABLE payment (
    pay_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    reserve_id BIGINT NOT NULL,
    amount INT NOT NULL COMMENT '결제 금액',
    is_pay BOOLEAN DEFAULT FALSE COMMENT '결제 여부(true / false)',
    create_at DATETIME NOT NULL COMMENT '생성 시간'
) COMMENT = '결제 정보';

CREATE TABLE amount_history (
    point_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    amount INT NOT NULL COMMENT '결제 금액',
    point_type VARCHAR(10) NOT NULL COMMENT '결제 타입 (CHARGE, USE)',
    create_at DATETIME NOT NULL COMMENT '생성 일'
) COMMENT = '금액 이력';