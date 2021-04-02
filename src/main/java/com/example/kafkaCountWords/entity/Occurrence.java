package com.example.kafkaCountWords.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Occurrence {
    public Integer lineOffset;
    public Integer charOffset;

    @Override
    public String toString() {
        return String.format("[lineOffset=%d, charOffset=%d]",lineOffset,charOffset);
    }
}

