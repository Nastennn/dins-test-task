package com.example.dins.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
public class User {
    public User(int id, String name, LocalDateTime timestamp){
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    public User() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return timestamp;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id + "," +
                "name:" + name + "," +
                "timestamp:" + timestamp +
                "}";
    }
}
