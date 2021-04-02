package com.example.kafkaCountWords.controller;

import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.MainReader;
import com.example.kafkaCountWords.entity.Occurrence;
import com.example.kafkaCountWords.entity.Occurrences;
import com.example.kafkaCountWords.kafka.KafkaProducer;
import com.example.kafkaCountWords.service.AggregatorService;
import com.example.kafkaCountWords.service.MainReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("main")
@Slf4j
public class MainController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    MainReader mainReader;

    @Autowired
    MainReaderService mainReaderService;

    @Autowired
    AggregatorService aggregatorService;

    @GetMapping("/get_results")
    public String getResults() {
        return aggregatorService.toString();
    }

    @PostMapping("/count")
    public ResponseEntity triggerCountWords() {
        try {
            mainReaderService.triggerMainReader();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("O.K", HttpStatus.OK);
    }

    @PostMapping("/msg_to_reader")
    public String sendMsgToReader() {
        IntStream.rangeClosed(1, 20).forEach(
                i -> kafkaProducer.sendMsgToMatcher(
                        new MainReader()
                                .setPageNumber(i)
                                .setPayload("payload " + i))
        );
        return "OK";
    }

    @PostMapping("/msg_to_aggregator")
    public String sendMsgToAggregator() {
        IntStream.rangeClosed(1, 4).forEach(
                i -> {
                    Occurrences occurrences = new Occurrences();
                    HashMap<String, Occurrences> oMap = new HashMap<>();
                    Aggregator aggregator = new Aggregator();

                    occurrences.addOccurrence(new Occurrence().setLineOffset(i).setCharOffset(i));
                    oMap.put(String.valueOf(i),occurrences);
                    aggregator.setWordCounter(oMap);

                    kafkaProducer.sendMsgToAggregator(aggregator);
                }
        );
        return "OK";
    }

}
