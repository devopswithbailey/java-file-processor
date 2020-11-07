package com.file.javafileprocessor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommonsCSV {
    private static final String[] HEADERS = {"author", "title"};

    private static final Map<String, String> AUTHOR_BOOK_MAP = new HashMap<String, String>() {
        {
            put("Dan Simmons", "Hyperion");
            put("Douglas Adams", "The Hitchhiker's Guide to the Galaxy");
        }
    };

    private static final Random random = new Random();

    ///Users/xhuang4/ProjectSpace/xhuang4/java-file-processor/src/main/resources/application.properties
    private static final String LARGE_FILE = "/Users/xhuang4/ProjectSpace/xhuang4/java-file-processor/src/main/resources/book_large.csv";

    private static final String LARGE_FILE_TOKENIZED = "/Users/xhuang4/ProjectSpace/xhuang4/java-file-processor/src/main/resources/book_large_tokenized.csv";

    private Instant start;

    public static void main(String[] args) throws IOException {
//        new CommonsCSV().readCSV();
//        new CommonsCSV().createCSVFile();
//        new CommonsCSV().createLargeCSVFile(LARGE_FILE);
        new CommonsCSV().readWriteCSV();
    }

    public void readWriteCSV() throws IOException {
        before();
        Reader in = new FileReader(LARGE_FILE);
        BufferedReader reader = new BufferedReader(in);
        FileWriter out = new FileWriter(LARGE_FILE_TOKENIZED);
        BufferedWriter writer = new BufferedWriter(out);
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(HEADERS));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(reader);
        int i=0;
        for (CSVRecord record : records) {
            String author = record.get("author");
            String title = record.get("title");
            printer.printRecord(author+"tokenized", title+"tokenized");
            i++;
        }
        System.out.println("Proccessed: "+i);
        after("Process");
    }

    public void readCSV(String file) throws IOException {
        before();
        Reader in = new FileReader(file);
        BufferedReader reader = new BufferedReader(in);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(reader);
        for (CSVRecord record : records) {
            String author = record.get("author");
            String title = record.get("title");
//            System.out.println(MessageFormat.format("author: {0}, title: {1}", author, title));
        }
        after("Read");
    }

    public void createCSVFile() throws IOException {
        FileWriter out = new FileWriter("C:\\WorkSpace\\spring-projects\\java-file-processor\\src\\main\\resources\\book_new.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            AUTHOR_BOOK_MAP.forEach((author, title) -> {
                try {
                    printer.printRecord(author, title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void createLargeCSVFile(String file) throws IOException {
        before();
        FileWriter out = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(out);
        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            for (int i = 0; i < 17000000; i++) {
                printer.printRecord("Douglas Adams" + i, "The Hitchhiker's Guide to the Galaxy" + i);
            }
        }
        after("Created");
    }

    public void after(String action) {
        Instant finish = Instant.now();
        monitorCPU();
        System.out.println(MessageFormat.format("After heap size: {0}", Runtime.getRuntime().totalMemory() / 1024 / 1024));
        System.out.println(MessageFormat.format("{1} the file in {0}ms", Duration.between(start, finish).toMillis(), action));

    }

    public void before() {
        monitorCPU();
        System.out.println(MessageFormat.format("Before heap size: {0}", Runtime.getRuntime().totalMemory() / 1024 / 1024));
        start = Instant.now();
    }

    public void monitorCPU() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        for (Long threadID : threadMXBean.getAllThreadIds()) {
            ThreadInfo info = threadMXBean.getThreadInfo(threadID);
            if (info.getThreadName().equals("main")) {
                System.out.println(String.format("CPU time: %s ns",
                        threadMXBean.getThreadCpuTime(threadID)));
            }
        }
    }
}
