package com.example.dins.kafka;

import com.example.dins.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaController {

    private final Producer producer;
    private final Consumer consumer;

    @Autowired
    KafkaController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public void sendMessageToKafkaTopic(User message) {
        this.producer.sendMessage(message);
    }

    public void startConsuming() {
        consumer.start();
    }

    public void stopConsuming() {
        consumer.stop();
    }

}