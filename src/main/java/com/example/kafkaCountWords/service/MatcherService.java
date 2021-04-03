package com.example.kafkaCountWords.service;

import com.example.kafkaCountWords.entity.Aggregator;
import com.example.kafkaCountWords.entity.MainReader;
import com.example.kafkaCountWords.entity.Occurrence;
import com.example.kafkaCountWords.entity.Occurrences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatcherService {

    @Value("${reader.buffer}")
    private Integer lineBufferSize;

    @Autowired
    Aggregator aggregator;

    @Autowired
    MatcherService matcherService;

    public Aggregator getAllOccurrences(MainReader mainReader, List<String> words) {
        if ( mainReader.getPrintResults() ) {
            return new Aggregator().setPrintResults(true);
        }
        HashMap<String, Occurrences> wordOccurrences = new HashMap<>();
        words.stream().forEach(word -> {
            wordOccurrences.put(word,matcherService.getWordOccurrences(mainReader, word));
        });
        return new Aggregator().setWordCounter(wordOccurrences);

    }
    public Occurrences getWordOccurrences(MainReader mainReader, String word) {
        Occurrences occurrences = new Occurrences();
        AtomicReference<Integer> lineItr = new AtomicReference<>(0);
        Arrays.stream(mainReader.getPayload().split("\n")).collect(Collectors.toList()).stream().forEach(line -> {
            int index = line.toLowerCase().indexOf(word.toLowerCase());
            if ( index != -1 ) {
                Occurrence occurrence = new Occurrence()
                        .setLineOffset(mainReader.getPageNumber() * lineBufferSize + lineItr.get())
                        .setCharOffset(index);
                occurrences.addOccurrence(occurrence);
                log.debug("{}: {}", word,occurrence.toString());
            }
            lineItr.updateAndGet(v -> v + 1);
        });
        return occurrences;
    }
}
