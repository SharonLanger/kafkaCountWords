package com.example.kafkaCountWords.kafka;

import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.MainReader;
import com.example.kafkaCountWords.service.AggregatorService;
import com.example.kafkaCountWords.service.MatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {

    @Value("${words}")
    private List<String> wordsArray;

    @Autowired
    MatcherService matcherService;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    AggregatorService aggregatorService;

    @KafkaListener(
            topics = "${topic.reader.name}",
            groupId = "group_main_reader",
            containerFactory = "concurrentKafkaListenerContainerFactoryMainReader",
            concurrency = "${topic.reader.partitions:1}")
    public void getMsgFromReaderTopic(
            MainReader mainReader,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {

        log.debug("getMsgFromReaderTopic: partition: {}. {}", partition, mainReader.toString());

        kafkaProducer.sendMsgToAggregator(matcherService.getAllOccurrences(mainReader, wordsArray));
    }

    @KafkaListener(
            topics = "${topic.aggregator.name}",
            groupId = "group_aggregator",
            containerFactory = "concurrentKafkaListenerContainerFactoryAggregator",
            concurrency = "${topic.aggregator.partitions:1}")
    public void getMsgFromAggregatorTopic(
            Aggregator aggregator,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        if ( aggregator.getPrintResults() ) {
            System.out.println(aggregatorService.toString());
            aggregatorService.deleteAllAggregations();
        } else {
            aggregatorService.combineAggregations(aggregator);
        }
        log.debug("getMsgFromAggregatorTopic: partition: {}. {}", partition, aggregator.toString());
    }

}
