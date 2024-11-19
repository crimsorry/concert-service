package hhplus.tdd.concert.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.common")
public class KafkaCustomProperty {

    private String bootstrapServers;

    // Producer
    private String keySerializer;
    private String valueSerializer;

    // Consumer
    private String keyDeserializer;
    private String valueDeserializer;
    private String consumerGroupId;
}