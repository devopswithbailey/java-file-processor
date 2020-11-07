package com.file.javafileprocessor;

import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.stream.Collectors;


public class BookProcessorString implements ItemProcessor<String, String> {
    public String process(String line) throws Exception {
        return Arrays.stream(line.split(",")).map(item -> item + "tokenized").collect(Collectors.joining(","));
    }
}
