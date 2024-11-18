package hhplus.tdd.concert.app.infrastructure;

import hhplus.tdd.concert.config.KafkaContainerExtension;
import hhplus.tdd.concert.config.RedisContainerExtension;
import hhplus.tdd.concert.config.TestContainerSupport;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.testcontainers.junit.jupiter.*;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@ExtendWith({
        KafkaContainerExtension.class
})
public class kafkaTest{


}
