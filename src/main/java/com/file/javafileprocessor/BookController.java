package com.file.javafileprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  Job job;

  @Autowired
  BatchConfigMultiThread batchConfig;

  @GetMapping("/ready1")
  public void ready1() throws Exception {
    perform("java-file-processor/src/main/resources/book.csv");
  }

  @GetMapping("/ready2")
  public void ready2() throws Exception {
    perform("java-file-processor/src/main/resources/book1.csv");
  }

  public void perform(String file) throws Exception {
    JobParameters params = new JobParametersBuilder()
        .addString("JobID", String.valueOf(System.currentTimeMillis()))
        .addString("filename", file)
        .toJobParameters();
    jobLauncher.run(job, params);
  }
}
