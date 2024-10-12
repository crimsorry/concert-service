**[ ERD ]**

```mermaid
erDiagram
    USER {
        bigint user_id PK
        varchar user_name "사용자 명"
        int amount "잔액"
    }
    USER_QUEUE {
        bigint queue_id PK
        bigint user_id FK
        varchar token "토큰 값"
        enum status "상태 값 (STAND_BY, ACTIVE, EXPIRED)"
        datetime create_at "생성 시간"
        datetime expired_at "만료 시간"
    }
    CONCERT {
        bigint concert_id PK
        varchar concert_title "콘서트 명"
        varchar concert_place "콘서트 장소"
    }
    CONCERT_SCHEDULE {
        bigint schedule_id PK
        bigint concert_id FK
        datetime open_date "콘서트 개최 일"
        datetime start_date "티켓 예매 시작 시간"
        datetime end_date "티켓 예매 종료 시간"
        int capacity "남은 좌석 수"
    }
    CONCERT_SEAT {
        bigint seat_id PK
        bigint schedule_id FK
        varchar seat_num "좌석 번호"
        int amount "좌석 금액"
        enum sear_status "좌석 점유 여부 (STAND_BY, RESERVED, ASSIGN)"
    }
    PAYMENT {
        bigint pay_id PK
        bigint user_id FK
        bigint reserve_id PK
        int amount "결제 금액"
        boolean is_pay "결제 여부(true / false)"
        datetime create_at "생성 시간"
    }
    RESERVATION {
        bigint reserve_id PK
        bigint user_id FK
        bigint seat_id FK
        varchar concert_title "콘서트 명"
        datetime open_date "콘서트 개최 일"
        varchar seat_num "좌석 번호"
        int amount "좌석 금액"
        enum reserve_statue "예약 상태 (PENDING, RESERVED, CANCELED)"
    }
    AMOUNT_HISTORY {
        bigint point_id PK
        bigint user_id FK
        int amount "결제 금액"
       	enum point_type "결제 타입 (CHARGE, USE)"
        datetime create_at "생성 일"
    }
	USER ||--o{ USER_QUEUE : "enter queue"
    USER ||--o{ PAYMENT : "has pay"
    USER ||--o{ RESERVATION : "has reserve"
    USER ||--o{ AMOUNT_HISTORY : "has point"
    CONCERT ||--o{ CONCERT_SCHEDULE : "has concert schedule"
    CONCERT_SCHEDULE ||--o{ CONCERT_SEAT : "has c oncert seat"
    PAYMENT ||--|| RESERVATION : "pay to reserve"
    CONCERT_SEAT ||--o{ RESERVATION : "has reserve"
    
```

* 사용자는 대기열을 여러건 생성 가능합니다. (대기열 만료 상태 ENUM 관리)
* 콘서트에는 콘서트 스케줄이 여러건 존재합니다. 
* 콘서트 스케줄 당 여러개의 좌석이 존재합니다.
* 콘서트 좌석 당 여러건의 예약이 존재합니다.
* 사용자는 콘서트를 여러건 예약 할 수 있습니다. 
* 사용자는 콘서트 예약를 확정하기 위해 좌석을 결제합니다. 
* 사용자에게는 결제 내역이 여러건 존재합니다.
* 사용자가 결제를 완료 하면 예약이 확정됩니다.
* 사용자가 결제를 완료하지 않으면 예약이 취소됩니다.
* 사용자는 충전, 사용 내역을 여러건 관리합니다.