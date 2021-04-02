package com.example.kafkaCountWords.kafka;

import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.MainReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Value("${topic.reader.name}")
    private String topicReader;

    @Value("${topic.reader.partitions}")
    private String readerPartitions;

    @Value("${topic.aggregator.name}")
    private String topicAggregator;

    @Value("${topic.aggregator.partitions}")
    private String aggregatorPartitions;

    @Autowired
    private KafkaTemplate<String, MainReader> kafkaTemplateMainReader;

    @Autowired
    private KafkaTemplate<String, Aggregator> kafkaTemplateAggregator;

    public void sendMsgToMatcher(MainReader mainReader) {

        kafkaTemplateMainReader.send(
                topicReader,
                getReaderPartition(mainReader.getPageNumber()),
                String.valueOf(mainReader.hashCode()),mainReader);

        log.debug("Sending massage to {}", topicReader);
        log.debug(mainReader.toString());
    }

    public void sendMsgToAggregator(Aggregator aggregator) {

        kafkaTemplateAggregator.send(
                topicAggregator,
                0,
                String.valueOf(aggregator.hashCode()),aggregator);

        log.debug("Sending massage to {}", topicAggregator);
        log.debug(aggregator.toString());
    }

    private Integer getReaderPartition(Integer pageNumber) {
        return pageNumber % Integer.parseInt(readerPartitions);
    }

}
