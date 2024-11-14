import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 150,           // 동시 사용자 수
    duration: '1m',     // 테스트 실행 시간
};

const BASE_URL = 'http://localhost:8080/api/v1';
const userIdStart = 36;

// 대기열 토큰 발급
function issueWaitingToken(userId) {
    let response = http.post(`${BASE_URL}/user/${userId}/queue/token/issue`);
    check(response, {
        'token issue status was 200': (r) => r.status === 200,
    });
    const responseBody = response.body ? response.json() : null;
    if (responseBody && responseBody.waitingToken) {
        return responseBody.waitingToken;
    } else {
        console.error('waitingToken is missing in response');
        return null;
    }
}

// 예약 가능 날짜 조회
function checkAvailableDates(concertId, waitingToken) {
    let headers = {
        'waitingToken': waitingToken, // 헤더에 waitingToken 추가
    };

    let response = http.get(`${BASE_URL}/concert/${concertId}/date`, { headers });
    check(response, {
        'available dates status was 200': (r) => r.status === 200,
    });
    return response;
}

export default function () {
    let userId = userIdStart + __VU; 
    let concertId = 451;           
    let waitingToken = issueWaitingToken(userId);

    if (waitingToken) {
        console.log(`WaitingToken for userId ${userId}: ${waitingToken}`);
    } else {
        console.error(`Skipping checkAvailableDates for userId ${userId} due to missing waitingToken`);
        return;
    }

    // 모든 토큰 발급 후 20초 대기
    sleep(20);

    for (let i = 0; i < 100; i++) {
        checkAvailableDates(concertId, waitingToken);
        sleep(1); // 호출 간격을 위해 1초 대기
    }
}