package hhplus.tdd.concert.config;

import hhplus.tdd.concert.config.property.KafkaCustomProperty;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaCustomProperty kafkaProperty;

    @Bean
    public KafkaConsumer<String, Object> kafkaConsumer() {
        Map<String, Object> properties = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperty.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProperty.getConsumerGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperty.getKeyDeserializer(),
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperty.getValueDeserializer(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
        return new KafkaConsumer<>(properties);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperty.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProperty.getConsumerGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperty.getKeyDeserializer(),
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperty.getValueDeserializer(),
                "spring.json.trusted.packages", "hhplus.tdd.concert.app.domain.*"
        );
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        return kafkaListenerContainerFactory;
    }

    @Bean
    public NewTopic waitingExpiredEventTopic() {
        return new NewTopic("waiting_expired_event", 1, (short) 2); // 파티션 1개, 복제본 2개
    }

    @Bean
    public NewTopic waitingExpiredTopic() {
        return new NewTopic("waiting_expired_time_event", 1, (short) 2); // 파티션 1개, 복제본 2개
    }
}