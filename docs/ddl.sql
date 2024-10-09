
-- concert

CREATE TABLE USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '����� ID',
    user_name VARCHAR(13) NOT NULL COMMENT '����� ��',
    charge INT NOT NULL default 0 COMMENT '�ܾ�' 
) COMMENT = '�����';

CREATE TABLE USER_QUEUE (
    queue_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL COMMENT '��ū ��',
    status VARCHAR(10) NOT NULL COMMENT '���� �� (STAND_BY, ACTIVE, DONE, EXPIRED)',
    entered_at DATE NOT NULL COMMENT '���Խð�',
    expired_at DATE COMMENT '����ð�',
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) COMMENT = '��⿭';

-- index �߰�
CREATE INDEX idx_user_queue_status ON USER_QUEUE(status);
CREATE INDEX idx_user_queue_entered_at ON USER_QUEUE(entered_at);

CREATE TABLE CONCERT (
    concert_id BIGINT PRIMARY KEY,
    concert_title VARCHAR(255) NOT NULL COMMENT '�ܼ�Ʈ ��',
    concert_place VARCHAR(255) NOT NULL COMMENT '�ܼ�Ʈ ���'
) COMMENT = '�ܼ�Ʈ ����';

CREATE TABLE CONCERT_SCHEDULE (
    schedule_id BIGINT PRIMARY KEY,
    concert_id BIGINT NOT NULL,
    open_date DATE NOT NULL COMMENT '�ܼ�Ʈ ���� ��',
    start_date DATE NOT NULL COMMENT 'Ƽ�� ���� ���� �ð�',
    end_date DATE NOT NULL COMMENT 'Ƽ�� ���� ���� �ð�',
    FOREIGN KEY (concert_id) REFERENCES CONCERT(concert_id)
) COMMENT = '�ܼ�Ʈ ����';

CREATE TABLE CONCERT_SEAT (
    seat_id BIGINT PRIMARY KEY,
    schedule_id BIGINT NOT NULL,
    seat_num VARCHAR(3) NOT NULL COMMENT '�¼� ��ȣ',
    seat_amount INT NOT NULL COMMENT '�¼� �ݾ�',
    seat_status VARCHAR(10) NOT NULL COMMENT '�¼� ���� ���� (STAND_BY, RESERVED, ASSIGN)',
    FOREIGN KEY (schedule_id) REFERENCES CONCERT_SCHEDULE(schedule_id)
) COMMENT = '�ܼ�Ʈ �¼�';

-- index �߰�
CREATE INDEX idx_concert_seat_schedule_id ON CONCERT_SEAT(seat_status);

CREATE TABLE RESERVATION (
    reserve_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    concert_title VARCHAR(255) NOT NULL COMMENT '�ܼ�Ʈ ��',
    open_date DATE NOT NULL COMMENT '�ܼ�Ʈ ���� ��',
    seat_num VARCHAR(3) NOT NULL COMMENT '�¼� ��ȣ',
    seat_amount INT NOT NULL COMMENT '�¼� �ݾ�',
    reserve_status VARCHAR(10) NOT NULL COMMENT '���� ���� (PENDING, RESERVED, CANCELED)',
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (schedule_id) REFERENCES CONCERT_SCHEDULE(schedule_id),
    FOREIGN KEY (seat_id) REFERENCES CONCERT_SEAT(seat_id)
) COMMENT = '���� ����';


CREATE TABLE PAYMENT (
    pay_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reserve_id BIGINT NOT NULL,
    pay_amount INT NOT NULL COMMENT '���� �ݾ�',
    is_pay BOOLEAN DEFAULT FALSE COMMENT '���� ����(true / false)',
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (reserve_id) REFERENCES RESERVATION(reserve_id) 
) COMMENT = '���� ����';










