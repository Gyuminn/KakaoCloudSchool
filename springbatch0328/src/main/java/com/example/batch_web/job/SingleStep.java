package com.example.batch_web.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //모든 JOB은 Spring Bean으로 등록되어야 합니다.
@EnableBatchProcessing //이 클래스의 Bean이 배치 작업으로 동작할 수 있는 Bean이라는 설정
@Log4j2
@RequiredArgsConstructor
public class SingleStep {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    //수행할 작업을 작성
    public Step StartStep(){
        return stepBuilderFactory.get("StartStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("시작 스텝");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    //수행할 작업을 작성
    public Step NextStep(){
        return stepBuilderFactory.get("NextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("두번째 스텝");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    //수행할 작업을 작성
    public Step LastStep(){
        return stepBuilderFactory.get("LastStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("마지막 스텝");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job MultiStepJob(){
        return jobBuilderFactory.get("multistepjob")
                .incrementer(new RunIdIncrementer())
                .start(StartStep())
                .next(NextStep())
                .next(LastStep())
                .build();
    }
}
