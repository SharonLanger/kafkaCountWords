package com.example.kafkaCountWords.entity;

import com.example.kafkaCountWords.KafkaCountWordsApplicationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MainReaderTest extends KafkaCountWordsApplicationTests {
    @Autowired
    MainReader mainReader;

    @BeforeEach
    void setUp() {
        mainReader.setPageNumber(3).setPayload("I'm a payload");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sandbox() {
        System.out.println(mainReader.toString());
        assertThat(true).isTrue();
    }
}