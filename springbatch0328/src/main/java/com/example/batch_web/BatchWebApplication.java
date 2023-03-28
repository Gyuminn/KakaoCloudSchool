package com.example.batch_web;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class BatchWebApplication {
    //Job 과 Step을 생성할 수 있는 팩토리 클래스를 주입
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step(){
        return this.stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(
                            StepContribution contribution,
                            ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello Spring Batch");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job job(){
        //이 작업을 다시 한 번 실행하고자 하면 job의 이름을 수정해주거나
        //sequence를 적용해야 합니다.
        return jobBuilderFactory.get("job3")
                .incrementer(new RunIdIncrementer())
                .start(step()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchWebApplication.class, args);
    }

}
