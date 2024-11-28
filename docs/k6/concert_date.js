import http from 'k6/http';
import { check, sleep, fail } from 'k6';

export let options = {
    startVUs: 0, // 시작 VU 수
    stages: [
        { duration: '15s', target: 50 },
        { duration: '15s', target: 80 },
        { duration: '15s', target: 50 }
    ],
    thresholds: {
        http_req_duration: ['p(90)<2000', 'p(95)<2000'], // 90% : 2초 미만/ 99% : 2초 미만
        http_req_failed: ['rate<0.01'],                  // 실패율 : 1% 미만
        checks: ['rate>0.99'],                           // 전체 요청 성공률 : 99% 이상
    },
    ext: {
      influxdb: {
        enabled: true,
        address: 'http://localhost:8086', 
        database: 'k6'
      },
    }
};

const BASE_URL = 'http://localhost:8081/api/v1';
const userIdStart = 36;
const MAX_RETRIES = 5;

// 대기열 토큰 발급
function issueWaitingTokenWithRetry(userId) {
    let retries = 0;
    let response;
    let success = false;

    while (retries < MAX_RETRIES && !success) {
        response = http.post(`${BASE_URL}/user/${userId}/queue/token/issue`);
        success = check(response, {
            '토큰 발급 성공': (r) => r.status === 200,
        }); 

        if (!success) {
            retries++;
            sleep(1); 
        }
    }
    
    const responseBody = response && response.body ? response.json() : null;

    if (!success) {
        fail(`UserId: ${userId} 재시도 횟수: ${MAX_RETRIES}, 상태 코드 ${responseBodystatus}, 응답: ${responseBody.body}`);
    }

    return responseBody ? responseBody.waitingToken : null;
}

// 예약 가능 날짜 조회
function checkAvailableDatesWithRetry(concertId, waitingToken) {
    let retries = 0;
    let response;
    let success = false;

    while (retries < MAX_RETRIES && !success) {
        let headers = {
            'waitingToken': waitingToken, 
        };

        response = http.get(`${BASE_URL}/concert/${concertId}/date`, { headers });
        success = check(response, {
            '예약 가능 날짜 조회 성공': (r) => r.status === 200,
        });

        if (!success) {
            retries++;
            sleep(1); 
        }
    }

    if (!success) {
        const responseBody = response && response.body ? response.json() : null;
        fail(`ConcertId: ${concertId}, 재시도 횟수: ${MAX_RETRIES}, 상태 코드: ${response.status}, 응답: ${responseBody}`);
    }

    return response;
}


export default function () {
    let userId = userIdStart + __VU; 
    let concertId = 451;           
    let waitingToken = issueWaitingTokenWithRetry(userId);

    // Step 1: 대기열 토큰 발급
    if (!waitingToken) {
        fail('대기열 토큰 없음')
    }

    // Step 2: (active 스케줄러 대기)
    sleep(40);

    // Step 3: 예약 가능 날짜 조회
    for (let i = 0; i < 5; i++) {
        checkAvailableDatesWithRetry(concertId, waitingToken);
        sleep(1); // 호출 간격
    }
}