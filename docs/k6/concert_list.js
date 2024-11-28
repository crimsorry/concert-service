import http from 'k6/http';
import { check, sleep, fail } from 'k6';

export let options = {
    startVUs: 0, // 시작 VU 수
    stages: [
    { duration: '15s', target: 500 },
    { duration: '15s', target: 900 },
    { duration: '15s', target: 500 },
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

const MAX_RETRIES = 5;

// 콘서트 목록 조회
function concertListRetry() {
    let retries = 0;
    let response;
    let success = false;

    while (retries < MAX_RETRIES && !success) {
        response = http.get(`${BASE_URL}/concerts/query`);
        success = check(response, {
            '콘서트 목록 조회 성공': (r) => r.status === 200,
        });

        if (!success) {
            retries++;
            sleep(1); 
        }
    }

    if (!success) {
        fail(`콘서트 목록 조회 실패: 재시도 횟수 ${MAX_RETRIES}, 상태 코드 ${response.status}, 응답: ${response.body}`);
    }
}

export default function () {  
    concertListRetry();
}
