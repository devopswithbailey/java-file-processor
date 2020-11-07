package com.file.javafileprocessor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableBatchProcessing
@Setter
@Getter
public class BatchConfigMultiThread {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${spring.batch.threads.size}")
    private int threadsSize;

//    private Resource inputResource=new FileSystemResource("java-file-processor/src/main/resources/book_large.csv");
    private Resource inputResource=new FileSystemResource("java-file-processor/src/main/resources/book.csv");

    private Resource outputResource = new FileSystemResource("java-file-processor/src/main/resources/output/outputData.csv");

    /**
     * JobBuilderFactory(JobRepository jobRepository)  Convenient factory for a JobBuilder which sets the JobRepository automatically
     */
    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory
                .get("readCSVFileJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    /**
     * StepBuilder which sets the JobRepository and PlatformTransactionManager automatically
     */

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step")
                .<Book, Book>chunk( 1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        System.out.println("batch threads size: "+threadsSize);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadsSize);
        executor.setMaxPoolSize(threadsSize);
        executor.setQueueCapacity(threadsSize);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("MultiThreaded-");
        return executor;
    }

    /**
     * Prints the Logs in the console.
     *
     * @return
     */

    @Bean
    public ItemProcessor<Book, Book> processor() {
        return new BookProcessor();
    }

    /**
     * FlatFileItemReader<T> Restartable ItemReader that reads lines from input setResource(Resource).
     *
     * @return
     */

    @Bean
    public FlatFileItemReader<Book> reader() {
        //Create reader instance
        FlatFileItemReader<Book> reader = new FlatFileItemReader<Book>();

        reader.setResource(inputResource);

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "author", "title"});
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
                    {
                        setTargetType(Book.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public FlatFileItemWriter<Book> writer() {
        FlatFileItemWriter<Book> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(outputResource);
        //All job repetitions should "append" to same output file
//        itemWriter.setAppendAllowed(true);

        //Name field values sequence based on object properties
        itemWriter.setLineAggregator(new DelimitedLineAggregator<Book>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Book>() {
                    {
                        setNames(new String[]{"author", "title"});
                    }
                });
            }
        });
        return itemWriter;
    }
}
