package com.example.kafkaCountWords;

import com.example.kafkaCountWords.controller.MainController;
import com.example.kafkaCountWords.service.MainReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class KafkaCountWordsApplicationTests {

    @Autowired
    MainController mainController;

    @Autowired
    MainReaderService mainReaderService;

    @Test
    void mainRunnerTest() throws IOException, InterruptedException {
        int sec = 1000;

        mainController.triggerCountWords();
//        mainReaderService.triggerMainReader();

        Thread.sleep(sec * 5);
    }

}
