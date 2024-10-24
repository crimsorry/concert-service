package hhplus.tdd.concert.common.log;

import hhplus.tdd.concert.common.config.log.MaskingConvertor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class MaskingConvertorUnitTest {

    @InjectMocks
    private MaskingConvertor maskingConvertor;

    @Test
    public void 개인정보_마스킹() {
        // given
        String originalMessage = "{\"memberName\":\"김소리\",\"concertTitle\":\"드라큘라\"}";
        String expectedMaskedMessage = "{\"memberName\":\"김*리\",\"concertTitle\":\"드라큘라\"}";

        // then & when
        String maskedMessage = maskingConvertor.masking(originalMessage);

        // 결과 검증
        assertEquals(expectedMaskedMessage, maskedMessage);
    }

    @Test
    public void 개인정보_마스킹_데이터_없음() {
        // given
        String originalMessage = "{\"concertTitle\":\"드라큘라\"}";
        String expectedMessage = "{\"concertTitle\":\"드라큘라\"}";

        // then & when
        String maskedMessage = maskingConvertor.masking(originalMessage);

        // 결과 검증
        assertEquals(expectedMessage, maskedMessage);
    }

    @Test
    public void 길이_500_이상_데이터_컷() {
        // given
        StringBuilder longResponse = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            longResponse.append("a");
        }

        // then & when
        String truncatedResponse = maskingConvertor.truncateResponse(longResponse.toString());

        // 결과 검증
        assertEquals(500, truncatedResponse.length());
        assertTrue(truncatedResponse.endsWith("...}"));
    }

    @Test
    public void 길이_500_이하_데이터_반환() {
        // given
        String shortResponse = "{\"memberName\":\"김소리\",\"concertTitle\":\"드라큘라\"}";

        // then & when
        String truncatedResponse = maskingConvertor.truncateResponse(shortResponse);

        // 결과 검증
        assertEquals(shortResponse, truncatedResponse);
    }

}
