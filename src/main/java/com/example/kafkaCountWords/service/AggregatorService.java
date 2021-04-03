package com.example.kafkaCountWords.service;


import com.example.kafkaCountWords.entity.Aggregator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data
public class AggregatorService {

    @Autowired
    Aggregator aggregations = new Aggregator();

    public void combineAggregations(Aggregator aggregator) {
        aggregations.combineWordCounter(aggregator);
    }

    public void deleteAllAggregations() {
        aggregations.deleteAllWordCounters();
    }

//    @Override
//    public String toString() {
//        return aggregations.toString();
//    }
}
