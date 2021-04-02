package com.example.kafkaCountWords.service;

import com.example.kafkaCountWords.entity.MainReader;
import com.example.kafkaCountWords.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
@Slf4j
public class MainReaderService {

    @Value("${reader.buffer}")
    private Integer lineBufferSize;

    @Value("${input.file}")
    private String inputFileName;

    @Autowired
    public MainReader mainReader;

    @Autowired
    KafkaProducer kafkaProducer;

    public void triggerMainReader() throws IOException {
        AtomicReference<Integer> itr = new AtomicReference<>(0);
        List<String> batchLines = new ArrayList<>();
        try (final Stream<String> lines = Files.lines(Path.of("src/main/resources/" + inputFileName))) {
            lines.forEach(l -> {
                batchLines.add(l);
                if ( batchLines.size() == lineBufferSize ) {
                    sendMsgToKafka(itr, batchLines,false);
                }
            });
        } finally {
            if (batchLines.size() != 0 ) {
                sendMsgToKafka(itr, batchLines, false);
            }
            sendMsgToKafka(new AtomicReference<>(0), new ArrayList<>(), true);
        }
    }

    private void sendMsgToKafka(AtomicReference<Integer> itr, List<String> batchLines, Boolean isLast) {
        kafkaProducer.sendMsgToMatcher(
            mainReader
                .setPayload(String.join("\n", batchLines))
                .setPageNumber(itr.get())
                .setPrintResults(isLast)
        );
        batchLines.clear();
        itr.getAndSet(itr.get() + 1);
    }
}
