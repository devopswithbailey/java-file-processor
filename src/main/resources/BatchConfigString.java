package com.file.javafileprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfigString {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

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
                .<String, String>chunk(8192)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    /**
     * Prints the Logs in the console.
     *
     * @return
     */

    @Bean
    public ItemProcessor<String, String> processor() {
        return new BookProcessorString();
    }

    /**
     * FlatFileItemReader<T> Restartable ItemReader that reads lines from input setResource(Resource).
     *
     * @return
     */

    @Bean
    public FlatFileItemReader<String> reader() {
        //Create reader instance
        FlatFileItemReader<String> reader = new FlatFileItemReader<String>();

        reader.setResource(inputResource);

        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
//                setLineTokenizer(new DelimitedLineTokenizer() {
//                    {
//                        setNames(new String[] { "author", "title"});
//                    }
//                });
//                //Set values in Employee class
//                setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
//                    {
//                        setTargetType(Book.class);
//                    }
//                });
            }
        });
        return reader;
    }

    @Bean
    public FlatFileItemWriter<String> writer() {
        FlatFileItemWriter<String> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(outputResource);
        //All job repetitions should "append" to same output file
        itemWriter.setAppendAllowed(true);

        //Name field values sequence based on object properties
        itemWriter.setLineAggregator(new DelimitedLineAggregator<String>() {
            {
//                setDelimiter(",");
//                setFieldExtractor(new BeanWrapperFieldExtractor<String>() {
//                    {
//                        setNames(new String[]{"author", "title"});
//                    }
//                });
            }
        });
        return itemWriter;
    }
}
