package com.file.javafileprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

//@EnableScheduling
@Component
@SpringBootApplication
public class JavaFileProcessorApplication {


	public static void main(String[] args) {
		SpringApplication.run(JavaFileProcessorApplication.class, args);
	}





}
