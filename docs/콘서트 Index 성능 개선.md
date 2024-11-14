# 🧩 콘서트 Index 성능 보고서



## 🧩 테스트 환경

- OS: Window
- 성능테스트: Explain Analyze, k6



## 🧩 Index 성능 개성 결과

### 사용 케이스: 콘서트 날짜 조회

- **테스트 케이스**: 1,000,000건 데이터로 150명의 유저가 100번 조회

  **결과 요약**:

  - **초당 조회수**: 분당 약 15,000회
  - **전반적 성능 개선율**: **약 28.72% 성능 개선**

| 케이스             | no index | index    | 개선률                  |
| ------------------ | -------- | -------- | ----------------------- |
| **요청 블록 시간** | 4.17ms   | 4.42ms   | 약 **-5.99% 성능 저하** |
| **연결 시간**      | 2.6ms    | 975.31µs | 약 **62.5% 개선**       |
| **전체 요청 시간** | 14.87초  | 15.17초  | 약 **-2.02% 성능 저하** |
| **응답 수신 시간** | 675.97µs | 427.17µs | 약 **36.82% 개선**      |
| **요청 전송 시간** | 4.53ms   | 767.86µs | 약 **83.04% 개선**      |
| **응답 대기 시간** | 14.87초  | 15.17초  | 약 **-2.02% 성능 저하** |
| **결과**           |          |          | **28.72% 개선**         |

* 일부 지표에서 성능 저하도 발견했지만, 전체적으로 성능 개선 확인.



## 🧩 Index 란?

조회 성능을 높이기 위해 쓰인다. 특히, **카디널리티**가 높은 경우(즉, 중복도가 낮은 경우) 인덱스의 효과가 극대화된다. 카디널리티가 높다는 것은 해당 컬럼의 값이 다양하게 분포되어 있어, 인덱스를 사용할 때 검색 성능을 크게 향상시킬 수 있음을 의미한다.



## 🧩 Index 적용범위

- FK (모든 테이블)
- 상태 코드(대기열, 좌석, 예약, 포인트 사용 내역)



> **상태코드**
>
> 상태코드의 경우 update 가 자주 일어나긴 하지만, DB의 트랜잭션 격리 수준으로 인해 재 열려 있는 트랜잭션 이후에 시작되는 트랜잭션에는 영향을 미치지 않는다. 따라서 조회가 자주 일어나는 상태코드에 Index 를 걸었다.



```
FK 와 상태코드에 index 를 건 결과 전체적으로 성능이 향상된 것을 확인 할 수 있었다.

index 성능 테스트의 경우 가장 조회가 많이 일어날 것 같은 예약 가능 날짜 조회를 이용하여 비교해 보았다.

콘서트 조회 역시 사용자들이 main 에서 가장 많이 조회하게 되지만, 캐시를 걸어두었기 때문에 index 에 대한 성능 비교는 하지 않았다. 
```



## 🧩 Index 성능 실험 내역

### 1. 예약 가능 날짜 조회

```
- 기존 날짜 검색 로직 삭제 후, concertId 검색으로 변경
  기존 param: date
  변경 param: concertId

인터파크, yes24 등의 콘서트 예매 사이트의 경우 콘서트 정보 조회 후 콘서트 상세 페이지에서 날짜를 클릭한다.
따라서 캘린더에 존재하는 날짜에 대한 정보는 클라이언트에서 표출한다고 가정 후
서버에서는 콘서트에 대한 선택 가능한 전체 날짜 리스트로 변경
```



* **[no index : Explain]**
  * 스케줄: Full Scan, 좌석: range


![image-20241114032407423](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114032407423.png)

* **[no index : Explain Analyze]**
  * 실행시간: 0.325ms
  * 비용: 64263

```sql
-> Limit: 10 row(s)  (cost=64263..64264 rows=10) (actual time=325..325 rows=3 loops=1)
    -> Table scan on <temporary>  (cost=64263..64325 rows=4695) (actual time=324..324 rows=3 loops=1)
        -> Temporary table with deduplication  (cost=64263..64263 rows=4695) (actual time=324..324 rows=3 loops=1)
            -> Limit table size: 10 unique row(s)
                -> Nested loop inner join  (cost=63794 rows=4695) (actual time=14.7..324 rows=150 loops=1)
                    -> Filter: (cs2_0.seat_status = 'STAND_BY')  (cost=47361 rows=46952) (actual time=0.0494..156 rows=500107 loops=1)
                        -> Table scan on cs2_0  (cost=47361 rows=469518) (actual time=0.0465..110 rows=500112 loops=1)
                    -> Filter: (cs1_0.concert_id = 451)  (cost=0.25 rows=0.1) (actual time=259e-6..259e-6 rows=300e-6 loops=500107)
                        -> Single-row index lookup on cs1_0 using PRIMARY (schedule_id=cs2_0.schedule_id)  (cost=0.25 rows=1) (actual time=105e-6..129e-6 rows=1 loops=500107)
```



* **[no index : K6]**

  * 최대 user: 150
  * 조회: 10번  
  * 분당 1,500

  ```
  user 100, 150, 200 명으로 테스트를 실행시켰다. 
  
  결과적으로 200 명에서 DB Connection 오류가 나타났다. 
  데이터베이스에 트래픽이 몰려 6%의 사용자가 조회에 실패하게 된 것. 
  
  - DB Connection Pool 한계
  동시 접속자 수가 많아지게 되면 한정된 커넥션이 빠르게 소진됨으로 일부 요청이 실패된 것으로 확인된다.
  인덱스를 추가해 user 가 증가되는지 테스트 해보려고 한다.
  물론 DB Connection 이 높아진다고 해서 성능이 무조건 좋아지지는 않지만, 테스트를 위해 진행하려고 한다. (Disk 병목 등) 
  
  +)
  혹시 동시접속자수가 조회 성능에 영향이 미치는지 궁금해 호출 횟수만 늘려 테스트도 진행해보았다.
  ```

[user100]

![image-20241114035041111](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114035041111.png)

[user150]

![image-20241114035614438](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114035614438.png)

[user200]

![image-20241114035327933](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114035327933.png)

* **[no index : K6]**

  * 최대 user: 150 
  * 조회: 100번  
  * 분당 15,000

  ```
  user 수는 150 명으로 고정하고 조회 횟수를 늘려가며(15, 50, 100) 테스트 했다. 
  
  테스트 결과 동시접속자 수 가 성능에 영향을 미치는 것으로 확인되었다.
  조회 횟수가 증가하게 되면 DB 쿼리 비용이 누적되 서버에 부하가 발생 할 수 있지만 
  캐시를 사용하는 경우 실시간성이 떨어지기 때문에 매진된 날짜 정보가 표출될 수 있다.
  
  따라서 조회 횟수를 측정해본 결과 100번으로 결론되었다.
  ```

[조회: 15]

![image-20241114040357522](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114040357522.png)

[조회 50]

![image-20241114041746540](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114041746540.png)

[조회 100]

![no_index_select_date_k6_150_100](C:\Users\user\Desktop\k6\picture\no_index_select_date_k6_150_100.png)



* **[index : Explain- (seat_status, schedule_id)]**
  * 스케줄:  eq_ref, 좌석: ref (index 범위 스캔)


![image-20241114130054156](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114130054156.png)

* **[index : Explain Analyze]**

  * 실행시간: 0.299ms

  * 비용: 226962

```sql
-> Limit: 10 row(s)  (cost=226962..226962 rows=10) (actual time=299..299 rows=3 loops=1)
    -> Table scan on <temporary>  (cost=226962..227258 rows=23476) (actual time=299..299 rows=3 loops=1)
        -> Temporary table with deduplication  (cost=226962..226962 rows=23476) (actual time=299..299 rows=3 loops=1)
            -> Limit table size: 10 unique row(s)
                -> Nested loop inner join  (cost=224614 rows=23476) (actual time=13.7..298 rows=150 loops=1)
                    -> Covering index lookup on cs2_0 using idx_seat_status_schedule (seat_status='STAND_BY')  (cost=24849 rows=234759) (actual time=0.0457..132 rows=500107 loops=1)
                    -> Filter: (cs1_0.concert_id = 451)  (cost=0.751 rows=0.1) (actual time=255e-6..255e-6 rows=300e-6 loops=500107)
                        -> Single-row index lookup on cs1_0 using PRIMARY (schedule_id=cs2_0.schedule_id)  (cost=0.751 rows=1) (actual time=102e-6..126e-6 rows=1 loops=500107)
```



* **[index : K6]**

  * 최대 user: 150

  * 조회: 10번  

  * 분당 1,500

  ```
  조회 횟수를 늘려 index 를 적용하지 않았을때와 index 를 적용하였을때의 속도 차이를 비교해보았다.
  (10, 100)
  
  index 를 적용하지 않았을때에 비교해 성능이 약 28% 이상 좋아진 것으로 확인. 
  ```

  [조회 10번 ]
  
  ![image-20241114124757970](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114124757970.png)
  
  [조회 100번]
  
  ![image-20241114132058330](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114132058330.png)



## 🧩 결론

* **빠른 데이터 접근**
  인덱스를 적용하지 않았을 때는 전체 테이블을 스캔해야 했기 때문에, 원하는 데이터를 찾기 위해 모든 행을 조회해야 했다. 반면 인덱스를 적용하면, 필요한 조건에 맞는 데이터만 선택적으로 접근할 수 있다. 이로 인해 조회할 데이터의 양이 줄어들고, 탐색 시간이 크게 단축되었다.

* **비용 증가했는데 성능 개선?**

  데이터베이스 옵티마이저는 예상 비용(cost)을 기반으로 실행 계획을 선택하지만, 비용은 예상치일 뿐 실제 실행 시간과 항상 일치하지 않는다는 것 확인. 쿼리 최적화의 목적은 비용을 줄이는 것이 아니라, 실제 실행 시간을 줄이는 데 있다.

* **커넥션 풀 효율성 증가**

  쿼리 속도가 빨라지면서 데이터베이스 커넥션 점유 시간이 줄어들었고, 그로 인해 **요청 블록 시간**과 **연결 시간**이 감소했다. 이는 **다른 요청들이 대기 상태에서 더 빨리 벗어날 수 있도록 하여 커넥션 풀의 효율성을 높이는 효과**로 이어졌다.



## 🧩 기타 테스트

index 에 대해 학습하고자 추가적인 테스트 진행

```sql
-- 0.324 cost: 85918 type: All
CREATE INDEX idx_concert_id ON concert_schedule(concert_id);

> 쿼리 옵티마이저가 index 를 사용하지 못하고 full scan 발생. 
단일쿼리를 사용하였기 때문에 inner join 에서 전체 수행을 하게 되는 걸까
라는 고민에 schedule_id 와 함께 복합 인덱스를 적용해 쿼리 수행

-- 시간: 0.326, cost 82909 : type: All
CREATE INDEX idx_concert_id_schedule ON concert_schedule (concert_id, schedule_id);

> 복합인덱스를 사용했지만 여전히 쿼리 옵티마이저가 index 를 사용하지 못하고 full scan 발생. 
조건에 대한 (seat_status) index 를 걸지 않았기 때문에 인덱스 사용이 제한된 것으로 확인.
(카디널리티 중복과 상관없는 index 를 수행한 결과로 보인다.)

-- 시간: 0.955, cost 218550 type: ref
CREATE INDEX idx_seat_status ON concert_seat(seat_status);

> DB Connection pool 오류. 쿼리 조회 속도가 느려 오류 발생.
seat_status 만으로 index 를 걸었을때 성능이 궁금해 테스트를 진행하였으나 복합인덱스에 비해 느린 성능을 보인다.

-- 시간: 0.321, cost 83011 : type: index
CREATE INDEX idx_schedule_id_seat_status ON concert_seat (schedule_id, seat_status);

> 복합인덱스를 구성하였으나 카디널리티가 높은 데이터를 우선순위에 두었기 때문에 순차적인 스캔을 진행한것으로 보인다.
쿼리 조건과 인덱스 컬럼 순서가 완전히 일치하지 않으면 쿼리 옵티마이저가 인덱스를 비효율적으로 사용할 수 있다는 사실 확인
```

![image-20241114125615137](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20241114125615137.png)

