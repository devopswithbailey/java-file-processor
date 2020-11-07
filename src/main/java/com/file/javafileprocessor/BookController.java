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

  /*
  //    private Resource inputResource=new FileSystemResource("java-file-processor/src/main/resources/book_large.csv");
    private Resource inputResource=new FileSystemResource("java-file-processor/src/main/resources/book.csv");

    private Resource outputResource = new FileSystemResource("java-file-processor/src/main/resources/output/outputData.csv");

   */

  @GetMapping("/ready1")
  public void ready1() throws Exception {
//    batchConfig.setInputResource(new FileSystemResource("java-file-processor/src/main/resources/book.csv"));
    perform();
  }

//  @GetMapping("/ready2")
//  public void ready2() throws Exception {
//    batchConfig.setInputResource(new FileSystemResource("java-file-processor/src/main/resources/book_large.csv"));
//    perform();
//  }

  public void perform() throws Exception {
    JobParameters params = new JobParametersBuilder()
        .addString("JobID", String.valueOf(System.currentTimeMillis()))
        .toJobParameters();
    jobLauncher.run(job, params);
  }
}
