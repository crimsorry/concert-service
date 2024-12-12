# 콘서트 예약 서비스

## 🔑 주차 별 문제점 개선 회고
### [블로그 확인](https://velog.io/@crimsorry/series/%EC%BD%98%EC%84%9C%ED%8A%B8-%EC%8B%9C%EB%82%98%EB%A6%AC%EC%98%A4)

## 🔑 보고서
### [🗓️ 동시성 발생 분석 보고서](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%EB%8F%99%EC%8B%9C%EC%84%B1%20%EC%A0%9C%EC%96%B4%20%EB%B0%A9%EC%8B%9D%20%EC%84%B1%EB%8A%A5%20%ED%85%8C%EC%8A%A4%ED%8A%B8.md) 
### [🗓️ 쿼리 성능 분석 보고서](https://github.com/crimsorry/concert-service/blob/main/docs/%EC%BD%98%EC%84%9C%ED%8A%B8%20%EC%BF%BC%EB%A6%AC%20%EC%84%B1%EB%8A%A5%20%EA%B0%9C%EC%84%A0.md)
### [🗓️ Redis 캐싱 성능 테스트](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%EC%BD%98%EC%84%9C%ED%8A%B8%20%EC%BF%BC%EB%A6%AC%20%EC%84%B1%EB%8A%A5%20%EA%B0%9C%EC%84%A0.md)
### [🗓️ 분산 트랜잭션 설계](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98%20%EC%B2%98%EB%A6%AC%20%EB%B3%B4%EA%B3%A0%EC%84%9C.md)
### [🗓️ 부하 및 장애 대응 성능 테스트](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%EB%B6%80%ED%95%98%ED%85%8C%EC%8A%A4%ED%8A%B8%20%EC%84%B1%EB%8A%A5%20%EB%B3%B4%EA%B3%A0%EC%84%9C.md)

## 🔑 마일스톤

### [🗓️ 프로젝트 개요](https://github.com/crimsorry/hhplus-concert-service/issues/1)
### [🗓️ 프로젝트 일정](https://github.com/users/crimsorry/projects/2)

## 🔑 설계

### [🗓️ 동시성 선택 과정](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%EB%8C%80%EA%B8%B0%EC%97%B4%2C%20%EB%8F%99%EC%8B%9C%EC%84%B1%20%EC%B2%98%EB%A6%AC.md)
### [🗓️ 시퀀스 다이너그램](https://github.com/crimsorry/hhplus-concert-service/blob/docs/step5/docs/%EC%8B%9C%ED%80%80%EC%8A%A4%20%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.md)
### [🗓️ 플로우 차트](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%ED%94%8C%EB%A1%9C%EC%9A%B0%20%EC%B0%A8%ED%8A%B8.md)
### [🗓️ ERD](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/ERD.md)
### [🗓️ 패키지 구조](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/%ED%8C%A8%ED%82%A4%EC%A7%80%20%EA%B5%AC%EC%A1%B0.md)
### [🗓️ API 명세](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/API%20%EB%AA%85%EC%84%B8.yaml)
### [🗓️ Mock API 작성](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/Mock%20API%20%EC%9E%91%EC%84%B1.md)
### [🗓️ Swagger](https://github.com/crimsorry/hhplus-concert-service/blob/main/docs/Swagger.md)

## 🔑 [기술 스택]

### 애플리케이션
* **Framework**: Spring Boot 3.4
* **Architecture**: Clean Architecture + Layered Architecture
### 데이터
* **Database**: MySQL 8
* **DB ORM**: JPA (Hibernate)
* **Caching**: Redis
### 메시지 처리
* **Message Queue**: Kafka
### API 문서화
* **API Docs**: Spring Docs
### 테스트 및 품질
* **Test Framework**: JUnit, AssertJ
* **Load Testing**: k6
### 모니터링 및 로깅
* **Monitoring Tools**: Grafana, InfluxDB




