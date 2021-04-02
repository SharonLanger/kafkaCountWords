package com.example.kafkaCountWords.service;

import com.example.kafkaCountWords.KafkaCountWordsApplicationTests;
import com.example.kafkaCountWords.entity.MainReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainReaderServiceTest extends KafkaCountWordsApplicationTests {

    @Autowired
    MainReaderService mainReaderService;

    @Autowired
    MainReader mainReader;

    @Value("${topic.reader.name}")
    private String topicReader;

    @Value("${words}")
    private List<String> wordsArray;

    @Value("${topic.reader.partitions}")
    private String readerPartitions;

    @BeforeEach
    void setUp() {
        mainReader.setPageNumber(3).setPayload("I'm a payload");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readWordsArrayTest() {
        wordsArray.forEach(System.out::println);
        assertThat(wordsArray.contains("sharon")).isTrue();
        assertThat(wordsArray.contains("yoni")).isTrue();
    }
}