package com.example.batch_web.service;

import com.example.batch_web.domain.JPACustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JPAReaderService {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<JPACustomerEntity> jpaCustomerItemReader(EntityManagerFactory entityManagerFactory) {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("city", "Chicago");

        return new JpaPagingItemReaderBuilder<JPACustomerEntity>()
                .name("jpaCustomerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameterValues)
                .queryString("select c from JPACustomerEntity c where c.city = :city")
                .pageSize(3)
                .build();
    }

    @Bean
    public ItemWriter<JPACustomerEntity> jpaItemFileWriter() {
        // 파일에 기록
        FileSystemResource resource = new FileSystemResource(("output/test_output.csv"));

        return new FlatFileItemWriterBuilder<JPACustomerEntity>()
                .name("csvItemWriter")
                .resource(resource)
                .formatted()
                .format("%s %s lives at %s")
                .names(new String[]{"firstName", "lastName", "address"})
                .build();
    }

    @Bean
    public Step jpaStep() {
        return this.stepBuilderFactory.get("jpaStep2")
                .<JPACustomerEntity, JPACustomerEntity>chunk(10)
                .reader(jpaCustomerItemReader(null))
                .writer(jpaItemFileWriter())
                .build();
    }

    @Bean
    public Job jpaJob() {
        return this.jobBuilderFactory.get("jpajob2")
                .start(jpaStep())
                .build();
    }
}
