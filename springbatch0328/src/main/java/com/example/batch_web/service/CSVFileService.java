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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class CSVFileService {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // 읽기
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerDTO> customerDTOCSVItemReader() {
        // Resource 생성
        ClassPathResource resource = new ClassPathResource("input/customer.csv");
        // delimited는 구분자인데 , 가 기본이기 때문에 , 일 경우에는 아무것도 안적어도 됨.
        return new FlatFileItemReaderBuilder<CustomerDTO>()
                .name("customerItemReader")
                .delimited()
                .names(new String[]{"firstName", "middleInitial", "lastName", "addressNumber", "street", "city", "state", "zipCode"})
                .fieldSetMapper(new CustomerFieldSetMapper())
                .resource(resource)
                .build();
    }
    // 처리

    // 쓰기
    // Reader가 데이터를 읽으면 Processor에게 DTO의 Collection이 전달되고
    // Processor가 작업을 하고 나면 Writer에게 DTO의 Collection이 자동으로 전달됨.
    // Processor는 생략될 수 있음.
    @Bean
    public ItemWriter<CustomerDTO> CSVItemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

    // step
    @Bean
    public Step copyCSVStep() {
        // Bean 들어가는 애들은 조심해야 했다. 다 들어가기 때문에!
        // 근데 여기 보면 .get("copyFileStep")은 job(아래의 .get("csvjob"))의 메서드로 들어가기 때문에 괜찮다.
        // job 안에서 안겹치면 된다. -> 즉 csvjob.copyFileStep이 여러 개가 아니면 됨.
        return stepBuilderFactory.get("copyFileStep")
                .<CustomerDTO, CustomerDTO>chunk(10)
                .reader(customerDTOCSVItemReader())
                .writer(CSVItemWriter())
                .build();
    }

    // job - 단독으로 ApplicationContext에 등록
    // job을 만드는 메서드 이름이 ApplicationContext에 bean의 이름으로 등록
    // job을 만드는 메서드 이름은 중복되면 안된다.
    // step은 관계가 없는데 step은 Job 내부에 등록되기 때문에 Job 안에서 중복되지 않으면 된다.
    @Bean
    public Job csvJob() {
        return jobBuilderFactory.get("csvjob")
                .start(copyCSVStep())
                .build();
    }
}
