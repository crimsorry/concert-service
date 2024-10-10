package hhplus.tdd.concert.application.service.userqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserQueueServiceTest {

    @InjectMocks
    private UserQueueService userQueueService;

    @Test
    public void 유저_대기열_생성_성공() {
        // given
        long userId = 1L;

        // when
        String result = userQueueService.enqueueUser(userId);

        // then
        assertEquals("user-token", result);
    }
}
