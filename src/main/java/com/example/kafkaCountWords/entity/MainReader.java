package com.example.kafkaCountWords.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@Component
public class MainReader {
    private Integer pageNumber;
    private String payload;
    Boolean printResults = false;

    @Override
    public String toString() {
        return String.format("MainReader: pageNumber: %s, payload: %s",pageNumber.toString(),payload);
    }
}
