package com.example.kafkaCountWords.service;

import com.example.kafkaCountWords.KafkaCountWordsApplicationTests;
import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.Occurrence;
import com.example.kafkaCountWords.entity.Occurrences;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

class AggregatorServiceTest extends KafkaCountWordsApplicationTests {

    @Autowired
    AggregatorService aggregatorService;

    Aggregator aggregator1 = new Aggregator();
    Aggregator aggregator2 = new Aggregator();

    @BeforeEach
    void setUp() {
        Map<String, Occurrences> wordCounter1 = new HashMap<>();
        Map<String, Occurrences> wordCounter2 = new HashMap<>();

        Occurrences oList = new Occurrences();

        oList.getOccurrencesList().addAll(createOccurrences(Arrays.asList(1, 2)));
        wordCounter1.put("sharon", oList);
        oList = new Occurrences();

        oList.getOccurrencesList().addAll(createOccurrences(Arrays.asList(1, 2, 3)));
        wordCounter1.put("yoni", oList);
        oList = new Occurrences();

        aggregator1.setWordCounter(wordCounter1);

        oList.getOccurrencesList().addAll(createOccurrences(Arrays.asList(3)));
        wordCounter2.put("sharon", oList);
        oList = new Occurrences();

        oList.getOccurrencesList().addAll(createOccurrences(Arrays.asList(1, 2, 3)));
        wordCounter2.put("dana", oList);

        aggregator2.setWordCounter(wordCounter2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void combineAggregationsTest() {
        assertThat(aggregatorService.getAggregations().getWordCounter().size()).isEqualTo(0);
        aggregatorService.combineAggregations(aggregator1);
        assertThat(aggregatorService.getAggregations().getWordCounter().size()).isEqualTo(2);
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(1))).isTrue();
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(2))).isTrue();
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(3))).isFalse();

        aggregatorService.combineAggregations(aggregator2);
        assertThat(aggregatorService.getAggregations().getWordCounter().size()).isEqualTo(3);
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(1))).isTrue();
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(2))).isTrue();
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().contains(createOccurrence(3))).isTrue();

        assertThat(aggregatorService.getAggregations().getWordCounter().get("dana").getOccurrencesList().size()).isEqualTo(3);
        assertThat(aggregatorService.getAggregations().getWordCounter().get("sharon").getOccurrencesList().size()).isEqualTo(3);
        assertThat(aggregatorService.getAggregations().getWordCounter().get("yoni").getOccurrencesList().size()).isEqualTo(3);

    }

    private Occurrence createOccurrence(int i) {
        return new Occurrence().setLineOffset(i).setCharOffset(i);
    }

    private Collection<? extends Occurrence> createOccurrences(List<Integer> inputs) {
        Occurrences o = new Occurrences();
        inputs.forEach(i -> {
            o.addOccurrence(createOccurrence(i));
        });
        return o.getOccurrencesList();
    }
}