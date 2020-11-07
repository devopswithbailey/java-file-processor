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

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	private Instant start;

	public static void main(String[] args) {
		SpringApplication.run(JavaFileProcessorApplication.class, args);
	}

	/**
	 * This Job Will Run Once the all the beans are created and job will load the database with given csv file.
	 *
	 */
	@PostConstruct
	public void perform() throws Exception {
		JobParameters params = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		before();
		jobLauncher.run(job, params);
		after("Processed");
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
