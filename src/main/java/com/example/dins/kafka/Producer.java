package com.example.dins.kafka;

import com.example.dins.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Value("${spring.kafka.topic.name}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendMessage(User message) {
        logger.info(String.format("Producing message in topic %s -> %s", TOPIC, message));
        this.kafkaTemplate.send(TOPIC, message);
    }
}
