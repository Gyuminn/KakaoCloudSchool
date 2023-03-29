package com.example.batch_web.service;

import com.example.batch_web.dto.JDBCCustomerDTO;
import com.example.batch_web.dto.JDBCCustomerRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JDBCCustomerCursorService {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // Reader 생성
    @Bean
    public JdbcCursorItemReader<JDBCCustomerDTO> JdbcCustomerDTOItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<JDBCCustomerDTO>()
                .name("customerItemReader")
                .dataSource(dataSource)
                .sql("select * from customer where city=?")
                .rowMapper(new JDBCCustomerRowMapper())
                .preparedStatementSetter(citySetter())
                .build();
    }

    @Bean
    @StepScope
    public ArgumentPreparedStatementSetter citySetter() {
        return new ArgumentPreparedStatementSetter(new Object[]{"StamFord"});
    }

    // Writer 객체
    @Bean
    public ItemWriter<JDBCCustomerDTO> JdbcCustomerDTOItemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    // Step 생성
    @Bean
    public Step jdbcCopyFileStep() {
        return this.stepBuilderFactory.get("jdbcCopyFileStep")
                .<JDBCCustomerDTO, JDBCCustomerDTO>chunk(10)
                .reader(JdbcCustomerDTOItemReader(null))
                .writer(JdbcCustomerDTOItemWriter())
                .build();
    }

    @Bean
    public Job JdbcJob() {
        return this.jobBuilderFactory.get("jdbcJob")
                .start(jdbcCopyFileStep())
                .build();
    }
}
