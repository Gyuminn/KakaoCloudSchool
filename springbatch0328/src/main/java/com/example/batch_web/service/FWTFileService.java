package com.example.batch_web.service;

import com.example.batch_web.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class FWTFileService {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // Chunk는 3가지로 나누어짐.
    // 1. 데이터 읽기 작업
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerDTO> customerDTOFlatFileItemReader() {
        // resources 디렉토리의 input/customerFixedWidth 파일을 리소스로 변환
        ClassPathResource resource = new ClassPathResource("input/customerFixedWidth.txt");
        return new FlatFileItemReaderBuilder<CustomerDTO>()
                .name("customerItemReader")
                .resource(resource)
                .fixedLength()
                .columns(new Range[]{new Range(1, 11), new Range(12, 12), new Range(13, 22),
                        new Range(23, 26), new Range(27, 46), new Range(47, 62),
                        new Range(63, 64), new Range(65, 69)})
                .names(new String[]{"firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state", "zipCode"})
                .targetType(CustomerDTO.class)
                .build();
    }

    // 3. 데이터 저장 작업
    @Bean
    public ItemWriter<CustomerDTO> itemWriter() {
        return (items) -> items.forEach(System.out::println);
    }


    // 2. 데이터 가공 작업 - 필수가 아님
    // 스텝 생성
    @Bean
    public Step copyFileStep() {
        // Reader가 데이터를 10개씩 읽어서 Writer에 전달
        return stepBuilderFactory.get("copyFileStep")
                .<CustomerDTO, CustomerDTO>chunk(10)
                .reader(customerDTOFlatFileItemReader())
                .writer(itemWriter())
                .build();
    }

    // 잡생성
    @Bean
    public Job job1() {
        return jobBuilderFactory.get("flatfilejob")
                .start(copyFileStep())
                .build();
    }
}
