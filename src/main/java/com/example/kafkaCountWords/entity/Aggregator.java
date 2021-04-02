package com.example.kafkaCountWords.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@Component
public class Aggregator {
    private Map<String, Occurrences> wordCounter = new HashMap<>();
    Boolean printResults = false;

    public void combineWordCounter(Aggregator aggregator) {
        Map<String, Occurrences> wordCounterNew = aggregator.getWordCounter();

        wordCounterNew.forEach((k,v) -> {
            List<Occurrence> occurrencesListOld = wordCounter.getOrDefault(k,new Occurrences()).getOccurrencesList();
            occurrencesListOld.addAll(v.getOccurrencesList());
            wordCounter.put(k, new Occurrences().setOccurrencesList(occurrencesListOld));
        });
    }

    @Override
    public String toString() {
        return wordCounter.toString();
//        String result = "";
//        wordCounter.forEach((word, occurrences) -> {
//            result.concat(String.format("%s --> [%s]", word,occurrences));
//        });
//        return result;
    }
}
