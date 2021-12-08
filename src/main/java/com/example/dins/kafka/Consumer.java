package com.example.dins.kafka;

import com.example.dins.DatabaseController;
import com.example.dins.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.sql.SQLException;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private DatabaseController dbQuery;

    @Value("${spring.kafka.consumer.id}")
    private String consumerId;

    @Value("${spring.kafka.consumer.idleInterval}")
    private long idleInterval;


    @KafkaListener(id = "${spring.kafka.consumer.id}", topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", autoStartup = "${spring.kafka.consumer.autoStartup")
    public void consume(User user) {
        logger.info(String.format("Consuming message -> %s", user));
        try {
            logger.info(String.format("Inserting user -> %s", user));
            dbQuery.insertUser(user);
        } catch (SQLException e) {
            logger.info("Error accused while trying to insert user into db");
        }
    }

    public void start() {
        logger.info("Starting consumer");
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        listenerContainer.getContainerProperties().setIdleEventInterval(idleInterval);
        listenerContainer.start();
    }

    public void stop() {
        logger.info("Stopping consumer");
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        listenerContainer.stop();
    }
}
