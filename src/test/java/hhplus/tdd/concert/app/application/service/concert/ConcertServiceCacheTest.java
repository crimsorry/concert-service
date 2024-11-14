package hhplus.tdd.concert.app.application.service.concert;

import hhplus.tdd.concert.app.application.concert.dto.ConcertDTO;
import hhplus.tdd.concert.app.application.concert.service.ConcertService;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.domain.concert.repository.ConcertRepository;
import hhplus.tdd.concert.app.infrastructure.persistence.concert.dataaccess.jpa.ConcertJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ConcertServiceCacheTest {

    private final TestBase testBase = new TestBase();

    @Autowired
    private ConcertService concertService;

    @SpyBean
    private ConcertRepository concertRepository; // 호출 횟수 확인

    @SpyBean
    private ConcertJpaRepository concertJpaRepository;

    @Test
    public void 전체_콘서트_조회_2번조회시_캐시로_1번_호출_검증_() {
        when(concertRepository.findAll()).thenReturn(testBase.concerts);

        concertService.loadConcert();
        concertService.loadConcert();

        verify(concertJpaRepository, times(1)).findAll(); // 캐시 검증

        List<ConcertDTO> result = concertService.loadConcert();
        assertEquals(1, result.size());
        assertEquals(testBase.title, result.get(0).concertTitle());
    }

}
