package com.example.dins;

import com.example.dins.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DatabaseController {
    private final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
    private Connection connection;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.user.table.read.name}")
    private String readTable;

    @Value("${spring.datasource.user.table.write.name}")
    private String writeTable;

    public void connect(){
        try {
            logger.info("Trying to get connection to db");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e){
            logger.info("Error accused while trying to access db");
        }
    }

    public ResultSet selectAllUsers() throws SQLException{
        logger.info("Preparing statement for select all users");
        String SQL = "SELECT * FROM " + readTable;
        logger.info(String.format("Trying to create statement from string %s", SQL));
        Statement statement = connection.createStatement();
        logger.info("Executing statement");
        return statement.executeQuery(SQL);
    }

    public void insertUser(User user) throws SQLException {
        logger.info(String.format("Preparing statement for insert user id=%d", user.getId()));
        PreparedStatement prStatement = connection.prepareStatement("INSERT INTO "
                + writeTable + "(id, name, timestamp) VALUES(?, ?, ?) ON CONFLICT DO NOTHING");
        logger.info(String.format("Set variables values: id = %d, name = %s, time = %s", user.getId(), user.getName(), user.getTime()));
        prStatement.setInt(1, user.getId());
        prStatement.setString(2, user.getName());
        prStatement.setObject(3, user.getTime());
        logger.info("Executing statement");
        prStatement.executeUpdate();
    }
}
