//package hhplus.tdd.concert.app.application;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.stereotype.Component;
//import org.testcontainers.kafka.KafkaContainer;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class KafkaConsumerFactory {
//
//    private final String bootstrapServers;
//
//    public KafkaConsumerFactory(String bootstrapServers) {
//        this.bootstrapServers = bootstrapServers;
//    }
//
//    public KafkaConsumer<String, Object> createKafkaConsumer() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "retry-scheduler-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.springframework.kafka.support.serializer.JsonDeserializer");
//        props.put("spring.json.trusted.packages", "*");
//
//        KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList("waiting_expired_event.DLQ"));
//        return consumer;
//    }
//}
