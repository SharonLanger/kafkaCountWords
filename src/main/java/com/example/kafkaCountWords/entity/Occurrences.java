package com.example.kafkaCountWords.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Accessors(chain = true)
public class Occurrences {
    private List<Occurrence> occurrencesList = new ArrayList<>();

    public void addOccurrence(Occurrence occurrence) {
        occurrencesList.add(occurrence);
    }

    @Override
    public String toString() {
        return occurrencesList.toString() + "\n";
    }
}
