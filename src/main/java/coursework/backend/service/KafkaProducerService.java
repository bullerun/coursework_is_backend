package coursework.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "auth-logs"; // Название Kafka-топика

    public void sendLog(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}