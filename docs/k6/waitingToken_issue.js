import http from 'k6/http';
import { check, sleep, fail } from 'k6';

export let options = {
    startVUs: 0, // 시작 VU 수
    stages: [
    { duration: '15s', target: 200 },
    { duration: '15s', target: 450 },
    { duration: '15s', target: 200 },
    { duration: '15s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(90)<1000', 'p(95)<1000'], // 90% : 1초 미만/ 99% : 1초 미만
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

export default function () {
    let userId = userIdStart + __VU;    
    issueWaitingTokenWithRetry(userId);
}
