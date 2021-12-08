package com.example.dins;

import com.example.dins.domain.User;
import com.example.dins.kafka.KafkaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;


@SpringBootApplication
public class DinsTestApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private KafkaController kafkaController;

    @Autowired
    private DatabaseController dbQuery;

    @Value("${spring.kafka.consumer.id}")
    private String listenerId;

    private final Logger logger = LoggerFactory.getLogger(DinsTestApplication.class);

    private int mode;

    public static void main(String[] args) {
        SpringApplication.run(DinsTestApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Application started");
        System.out.println("Тестовое задание для Dins на вакансию Java Intern. Выполнила Зырянова Анастасия. \n " +
                "Пожалуйста, выберите режим запуска приложения. Для выбора введите цифру:\n" +
                "1 - Читает содержимое таблицы и пишет в Kafka topic; \n" +
                "2 - Получает все данные топика и пишет в таблицу");
        mode = getMode();
        logger.info(String.format("Getting the mode -> %d", mode));
        dbQuery.connect();
        if (mode == 1) {
            try {
                firstMode();
            } catch (SQLException e){
                logger.info("Error accuses while trying to read from db");
            }
        } else if (mode == 2) {
            secondMode();
        } else {
            wrongMode();
        }
    }

    public void wrongMode() {
        logger.info("Wrong number of mode. Should be 1 or 2");
        exitApp();
    }

    public void exitApp() {
        logger.info("Exiting the application");
        SpringApplication.exit(context);
    }

    public int getMode() {
        Scanner scanner = new Scanner(System.in);
        try {
            mode = scanner.nextInt();
        } catch (IllegalStateException | InputMismatchException e) {
            logger.info("Error accused while trying to read mode value");
        }
        return mode;
    }

    public void firstMode() throws SQLException {
        logger.info("Mode 1 started. Trying to read db");
        int i = 0;
        ResultSet rs = dbQuery.selectAllUsers();
        while (rs.next()) {
            i++;
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Timestamp timestamp = (Timestamp) rs.getObject("timestamp");
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            kafkaController.sendMessageToKafkaTopic(new User(id, name, dateTime));
        }
        logger.info(String.format("All %d users were send to Kafka topic", i));
        exitApp();
    }

    public void secondMode(){
        logger.info("Mode 2 started");
        kafkaController.startConsuming();
    }

    @EventListener
    public void eventHandler(ListenerContainerIdleEvent event) {
        if (event.getListenerId().startsWith(listenerId + "-")) {
            logger.info("Consumer has read all the messages from topic");
            kafkaController.stopConsuming();
            logger.info("Exiting the application");
            SpringApplication.exit(context);
        }
    }
}
